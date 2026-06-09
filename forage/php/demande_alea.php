<?php
$status    = isset($_GET['status'])    ? trim($_GET['status'])    : '';
$alerte    = isset($_GET['alerte'])    ? trim($_GET['alerte'])    : '';
$sort      = isset($_GET['sort'])      ? trim($_GET['sort'])      : 'asc';
$reference = isset($_GET['reference']) ? trim($_GET['reference']) : '';

$apiUrl = 'http://localhost:8080/forage/apis/demandes';

$params = [];
if ($status    !== '') $params['status']    = $status;
if ($alerte    !== '') $params['alerte']    = $alerte;
if ($sort      !== '') $params['sort']      = $sort;
if ($reference !== '') $params['reference'] = $reference;

if (!empty($params)) {
    $apiUrl .= '?' . http_build_query($params);
}

$ch = curl_init($apiUrl);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_TIMEOUT, 10);
$resp = curl_exec($ch);
$err  = curl_error($ch);
curl_close($ch);

$data = [];
if ($resp !== false) {
    $data = json_decode($resp, true);
    if (!is_array($data)) $data = [];
}

function h($s) {
    return htmlspecialchars((string)$s);
}

// ✅ Couleurs depuis AlerteColor Java
// niveau 1 = #d9ff00 (jaune), niveau 2 = #dc0e0e (rouge)
function getBadgeColor(int $niveau): string {
    return $niveau === 1 ? '#d9ff00' : '#dc0e0e';
}

function getBadgeTextColor(int $niveau): string {
    return $niveau === 1 ? '#333333' : '#ffffff'; // texte sombre sur jaune, blanc sur rouge
}

function hasAnyAlerte(array $demande): bool {
    foreach (($demande['intervals'] ?? []) as $it) {
        if (!empty($it['hasAlerte'])) return true;
    }
    return false;
}
?>

<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>Demandes en alerte</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; }

        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 8px; border: 1px solid #ddd; vertical-align: top; }
        th { background: #222; color: #fff; }

        .toolbar { margin-bottom: 15px; }

        .subtable {
            width: 100%;
            border-collapse: collapse;
            font-size: 12px;
            margin-top: 5px;
        }
        .subtable th { background: #444; color: white; }
        .subtable td, .subtable th { border: 1px solid #ccc; padding: 4px; }

        .badge-alerte {
            display: inline-block;
            padding: 3px 10px;
            border-radius: 4px;
            font-size: 11px;
            font-weight: bold;
        }

        .row-alerte td { border-left: 4px solid #dc0e0e00; }

        .error { color: red; }
    </style>
</head>

<body>

<h1>Demandes en alerte</h1>
<h1>ETU004185</h1>

<!-- ================= FILTRES ================= -->
<div class="toolbar">
    <form method="get">

        <label>
            Status :
            <input type="text" name="status" value="<?=h($status)?>" placeholder="id ou libellé">
        </label>

        <label style="margin-left:10px;">
            Référence :
            <input type="text" name="reference" value="<?=h($reference)?>" placeholder="REF-001">
        </label>

        <label style="margin-left:10px;">
            Alerte :
            <select name="alerte">
                <option value="">Tous</option>
                <option value="true"  <?= $alerte === 'true'  ? 'selected' : '' ?>>Avec alerte</option>
                <option value="false" <?= $alerte === 'false' ? 'selected' : '' ?>>Sans alerte</option>
            </select>
        </label>

        <label style="margin-left:10px;">
            Tri :
            <select name="sort">
                <option value="asc"  <?= $sort === 'asc'  ? 'selected' : '' ?>>Ancien → récent</option>
                <option value="desc" <?= $sort === 'desc' ? 'selected' : '' ?>>Récent → ancien</option>
            </select>
        </label>

        <button type="submit" style="margin-left:10px;">Filtrer</button>
    </form>
</div>

<?php if ($resp === false): ?>
    <div class="error">Erreur API : <?=h($err)?></div>
<?php else: ?>

<?php $nbAlerte = count(array_filter($data, 'hasAnyAlerte')); ?>
<p style="color:#555;">
    <?= $nbAlerte ?> demande(s) en alerte sur <?= count($data) ?> au total.
</p>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Référence</th>
            <th>Client</th>
            <th>Commune</th>
            <th>Date demande</th>
            <th>Intervalles</th>
        </tr>
    </thead>
    <tbody>

    <?php foreach ($data as $d):
        if (!hasAnyAlerte($d)) continue;
    ?>
        <tr class="row-alerte">
            <td><?= h($d['id']        ?? '') ?></td>
            <td><?= h($d['reference'] ?? '') ?></td>
            <td><?= h($d['client']    ?? '') ?></td>
            <td><?= h($d['commune']   ?? '') ?></td>

            <td>
                <?php if (!empty($d['dateDemande'])): ?>
                    <?php
                        $ts   = (int)($d['dateDemande'] / 1000);
                        $date = (new DateTime())->setTimestamp($ts);
                        echo h($date->format('Y/m/d H:i'));
                    ?>
                <?php else: ?>—<?php endif; ?>
            </td>

            <td>
                <?php if (!empty($d['intervals'])): ?>
                    <table class="subtable">
                        <thead>
                            <tr>
                                <th>De</th>
                                <th>À</th>
                                <th>Durée</th>
                                <th>Alerte</th>
                            </tr>
                        </thead>
                        <tbody>
                        <?php
                            $totalMinutes = 0;
                            foreach ($d['intervals'] as $it):
                                $dureeMin  = (int)($it['durationMinutes'] ?? 0);
                                $totalMinutes += $dureeMin;

                                $dureeLabel = round($dureeMin / 60, 2) . " h";
                                $hasAlerte = !empty($it['hasAlerte']);
                                $niveau    = isset($it['niveauAlerte']) ? (int)$it['niveauAlerte'] : 0;

                                $rowBg = $hasAlerte ? getBadgeColor($niveau) . '22' : 'transparent';
                        ?>
                            <tr style="background: <?= $rowBg ?>;">
                                <td><?= h($it['fromStatus'] ?? '—') ?></td>
                                <td><?= h($it['toStatus']   ?? '—') ?></td>
                                <td><?= h($dureeLabel) ?></td>
                                <td style="text-align:center;">
                                    <?php if ($hasAlerte && $niveau > 0): ?>
                                        <span class="badge-alerte" style="
                                            background: <?= getBadgeColor($niveau) ?>;
                                            color: <?= getBadgeTextColor($niveau) ?>;
                                        ">
                                            Niveau <?= $niveau ?>
                                        </span>
                                    <?php else: ?>
                                        <span style="color:#aaa;">—</span>
                                    <?php endif; ?>
                                </td>
                            </tr>
                        <?php endforeach; ?>
                        </tbody>
                        <tfoot>
                            <tr style="font-weight:bold; border-top:2px solid #aaa;">
                                <td colspan="2">Durée totale</td>
                                <td>
                                    <?php
                                        echo round($totalMinutes / 60, 2) . " h";
                                    ?>
                                </td>
                                <td></td>
                            </tr>
                        </tfoot>
                    </table>
                <?php else: ?>
                    —
                <?php endif; ?>
            </td>
        </tr>

    <?php endforeach; ?>

    </tbody>
</table>

<?php endif; ?>

</body>
</html>