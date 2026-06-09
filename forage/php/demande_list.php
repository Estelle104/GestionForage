<?php
// demande_list.php
// Simple client PHP qui appelle l'API REST `/api/demandes` et affiche un tableau

// Récupérer paramètres de filtre depuis la requête GET
$status = isset($_GET['status']) ? trim($_GET['status']) : '';
$alerte = isset($_GET['alerte']) ? trim($_GET['alerte']) : '';
$sort = isset($_GET['sort']) ? trim($_GET['sort']) : 'asc';

$apiUrl = 'http://localhost:8080/forage/api/demandes';
$params = [];
if ($status !== '') $params['status'] = $status;
if ($alerte !== '') $params['alerte'] = $alerte;
if ($sort !== '') $params['sort'] = $sort;
if (!empty($params)) $apiUrl .= '?' . http_build_query($params);

$ch = curl_init($apiUrl);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_TIMEOUT, 5);
$resp = curl_exec($ch);
$err = curl_error($ch);
curl_close($ch);

$data = [];
if ($resp !== false) {
    $data = json_decode($resp, true);
    if ($data === null) $data = [];
}

function h($s) { return htmlspecialchars((string)$s); }
?>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>Liste des Demandes (client PHP)</title>
    <style>
        table { width:100%; border-collapse: collapse; }
        th, td { padding:8px; border:1px solid #ddd; }
        th { background:#333; color:#fff; }
        .badge { display:inline-block; padding:4px 8px; border-radius:4px; font-size:12px; }
        .badge-alerte { background:#d9534f; color:#fff; }
        .badge-ok { background:#5cb85c; color:#fff; }
        .toolbar { margin-bottom:12px; }
    </style>
</head>
<body>
    <h1>Demandes</h1>
    <div class="toolbar">
        <form method="get">
            <label>Status: <input type="text" name="status" value="<?=h($status)?>" placeholder="id ou libellé"></label>
            <label style="margin-left:12px">Alerte:
                <select name="alerte">
                    <option value="">Tous</option>
                    <option value="true" <?= $alerte==='true' ? 'selected' : '' ?>>Avec alerte</option>
                    <option value="false" <?= $alerte==='false' ? 'selected' : '' ?>>Sans alerte</option>
                </select>
            </label>
            <label style="margin-left:12px">Tri:
                <select name="sort">
                    <option value="asc" <?= $sort==='asc' ? 'selected' : '' ?>>Chrono</option>
                    <option value="desc" <?= $sort==='desc' ? 'selected' : '' ?>>Inverse</option>
                </select>
            </label>
            <button type="submit">Filtrer</button>
        </form>
    </div>

    <?php if ($resp === false): ?>
        <div style="color:crimson">Erreur d'appel API: <?=h($err)?></div>
    <?php else: ?>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Référence</th>
                    <th>Client</th>
                    <th>Commune</th>
                    <th>Date demande</th>
                    <th>Statuts (chronologique)</th>
                    <th>Alertes</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($data as $d): ?>
                    <tr>
                        <td><?=h($d['id'] ?? '')?></td>
                        <td><?=h($d['reference'] ?? '')?></td>
                        <td><?=h($d['client'] ?? '')?></td>
                        <td><?=h($d['commune'] ?? '')?></td>
                        <td><?=h($d['dateDemande'] ?? '')?></td>
                        <td>
                            <?php if (!empty($d['statuses'])): ?>
                                <ul style="list-style:none; padding-left:0; margin:0">
                                <?php foreach ($d['statuses'] as $s): ?>
                                    <li>
                                        <strong><?=h($s['statusLibele'] ?? '')?></strong>
                                        <div style="font-size:12px; color:#666"><?=h($s['dateStatus'] ?? '')?></div>
                                    </li>
                                <?php endforeach; ?>
                                </ul>
                            <?php endif; ?>
                        </td>
                        <td>
                            <?php if (!empty($d['statuses'])): ?>
                                <?php
                                    $badges = [];
                                    foreach ($d['statuses'] as $s) {
                                        if (!empty($s['alertes'])) {
                                            foreach ($s['alertes'] as $a) {
                                                $active = !empty($a['isAlerte']);
                                                $label = ($a['statusLabel'] ?? '') . ' (' . ($a['dureeActuelle'] ?? 0) . '/' . ($a['dureeMinute'] ?? 0) . 'min)';
                                                $badges[] = ['label'=>$label,'active'=>$active];
                                            }
                                        }
                                    }
                                ?>
                                <?php if (empty($badges)): ?>—<?php else: ?>
                                    <?php foreach ($badges as $b): ?>
                                        <span class="badge <?= $b['active'] ? 'badge-alerte' : 'badge-ok' ?>"><?=
                                            h($b['label'])
                                        ?></span>
                                    <?php endforeach; ?>
                                <?php endif; ?>
                            <?php else: ?>—<?php endif; ?>
                        </td>
                    </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    <?php endif; ?>
</body>
</html>
