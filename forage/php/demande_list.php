<?php
// demande_list.php

$status = isset($_GET['status']) ? trim($_GET['status']) : '';
$alerte = isset($_GET['alerte']) ? trim($_GET['alerte']) : '';
$color  = isset($_GET['color']) ? trim($_GET['color']) : '';
$sort   = isset($_GET['sort']) ? trim($_GET['sort']) : 'asc';
$reference = isset($_GET['reference']) ? trim($_GET['reference']) : '';

$apiUrl = 'http://localhost:8080/forage/api/demandes';

$params = [];


if ($status !== '') $params['status'] = $status;
if ($alerte !== '') $params['alerte'] = $alerte;
if ($color !== '') $params['color'] = $color;
if ($sort !== '') $params['sort'] = $sort;
if ($reference !== '') $params['reference'] = $reference;


if (!empty($params)) {
    $params['status'] = trim($status);
    $params['reference'] = trim($reference);
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

function h($s)
{
    return htmlspecialchars((string)$s);
}
?>
<!doctype html>
<html>

<head>
    <meta charset="utf-8">
    <title>Demandes</title>

    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }

        th,
        td {
            padding: 8px;
            border: 1px solid #ddd;
            vertical-align: top;
        }

        th {
            background: #222;
            color: #fff;
        }

        .toolbar {
            margin-bottom: 15px;
        }

        .badge {
            display: inline-block;
            padding: 4px 8px;
            border-radius: 6px;
            font-size: 12px;
            margin: 2px;
            color: #000;

        }


        .error {
            color: red;
        }
    </style>
</head>

<body>

    <h1>Liste des demandes</h1>

    <div class="toolbar">
        <form method="get">

            <label>
                Status :
                <input type="text" name="status"
                    value="<?= h($status) ?>"
                    placeholder="id ou libellé">
            </label>

            <label style="margin-left:10px;">
                Référence :
                <input type="text" name="reference"
                    value="<?= h($reference) ?>"
                    placeholder="REF-001">
            </label>

            <label style="margin-left:10px;">
                Alerte :
                <select name="alerte">
                    <option value="">Tous</option>
                    <option value="false" <?= $alerte === 'false' ? 'selected' : '' ?>>Sans alerte</option>
                    <option value="true" <?= $alerte === 'true' ? 'selected' : '' ?>>Avec alerte</option>
                </select>
            </label>

            <label style="margin-left:10px;">
                Couleur :
                <input type="text" name="color"
                    value="<?= h($color) ?>"
                    placeholder="#FF0000 ou FF0000">
            </label>

            <label style="margin-left:10px;">
                Tri :
                <select name="sort">
                    <option value="asc" <?= $sort === 'asc' ? 'selected' : '' ?>>Ancien → récent</option>
                    <option value="desc" <?= $sort === 'desc' ? 'selected' : '' ?>>Récent → ancien</option>
                </select>
            </label>

            <button type="submit" style="margin-left:10px;">Filtrer</button>

        </form>
    </div>

    <?php if ($resp === false): ?>
        <div class="error">Erreur API : <?= h($err) ?></div>
    <?php else: ?>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Référence</th>
                    <th>Client</th>
                    <th>Commune</th>
                    <th>Date</th>
                    <th>Statuts (historique)</th>
                    <th>Alertes</th>
                </tr>
            </thead>

            <tbody>

                <?php foreach ($data as $d): ?>

                    <tr>
                        <td><?= h($d['id'] ?? '') ?></td>
                        <td><?= h($d['reference'] ?? '') ?></td>
                        <td><?= h($d['client'] ?? '') ?></td>
                        <td><?= h($d['commune'] ?? '') ?></td>
                        <td><?= h($d['dateDemande'] ?? '') ?></td>

                        <!-- STATUTS -->
                        <td>
                            <?php if (!empty($d['statuses'])): ?>
                                <ul style="padding-left:15px;">
                                    <?php foreach ($d['statuses'] as $s): ?>
                                        <li>
                                            <strong><?= h($s['statusLibele'] ?? '') ?></strong><br>
                                            <small><?= h($s['dateStatus'] ?? '') ?></small>
                                        </li>
                                    <?php endforeach; ?>
                                </ul>
                            <?php else: ?>
                                —
                            <?php endif; ?>
                        </td>

                        <!-- ALERTES -->
                        <td>
                            <?php
                            $badges = [];

                            if (!empty($d['statuses'])) {
                                foreach ($d['statuses'] as $s) {
                                    if (!empty($s['alertes'])) {
                                        foreach ($s['alertes'] as $a) {

                                            $active = !empty($a['isAlerte']);

                                            $label = ($a['statusLabel'] ?? '') .
                                                ' (' . ($a['dureeActuelle'] ?? 0) .
                                                '/' . ($a['dureeMinute'] ?? 0) . ' min)';

                                            $badges[] = [
                                                'label' => $label,
                                                'active' => $active,
                                                'color' => $a['couleur'] ?? '#999'
                                            ];
                                        }
                                    }
                                }
                            }
                            ?>

                            <?php if (empty($badges)): ?>
                                —
                            <?php else: ?>
                                <?php foreach ($badges as $b): ?>
                                    <span class="badge"
                                        style="background:<?= h($b['color']) ?>; opacity:<?= $b['active'] ? '1' : '0.5' ?>;">
                                        <?= h($b['label']) ?>
                                    </span>
                                <?php endforeach; ?>
                            <?php endif; ?>
                        </td>

                    </tr>

                <?php endforeach; ?>

            </tbody>
        </table>

    <?php endif; ?>

</body>

</html>