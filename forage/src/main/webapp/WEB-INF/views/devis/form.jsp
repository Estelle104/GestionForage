<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nouveau Devis - GestionForage</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f0f2f5;
            color: #2c3e50;
            min-height: 100vh;
        }
        .navbar {
            background: #2d2d2d;
            padding: 0 30px;
            display: flex;
            align-items: center;
            height: 56px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.15);
        }
        .navbar .brand { color:#fff; font-size:18px; font-weight:700; text-decoration:none; margin-right:40px; }
        .navbar .nav-links { display:flex; gap:0; list-style:none; }
        .navbar .nav-links a { color:#ccc; text-decoration:none; padding:16px 20px; font-size:14px; font-weight:500; transition:all .2s; border-bottom:3px solid transparent; }
        .navbar .nav-links a:hover { color:#fff; background:rgba(255,255,255,0.05); }
        .navbar .nav-links a.active { color:#fff; border-bottom-color:#f0c040; }

        .container { max-width: 820px; margin: 0 auto; padding: 30px 20px; }
        h1 { font-size:26px; color:#2c3e50; margin-bottom:25px; padding-bottom:12px; border-bottom:3px solid #3498db; }
        h2 { font-size:17px; color:#34495e; margin:28px 0 14px; font-weight:700; }

        .card { background:#fff; border-radius:10px; padding:28px; box-shadow:0 2px 15px rgba(0,0,0,0.08); margin-bottom:24px; }

        .form-group { margin-bottom:18px; }
        .form-group label { display:block; margin-bottom:5px; font-weight:600; font-size:13px; color:#444; }
        .form-group input, .form-group select, .form-group textarea {
            width:100%; padding:10px 13px; border:1.5px solid #dfe6e9; border-radius:6px;
            font-size:14px; transition:border-color .2s; background:#fff; color:#2c3e50;
        }
        .form-group textarea { resize:vertical; min-height:80px; }
        .form-group input:focus, .form-group select:focus, .form-group textarea:focus {
            outline:none; border-color:#3498db; box-shadow:0 0 0 3px rgba(52,152,219,.1);
        }
        .required::after { content:" *"; color:#e74c3c; }

        /* Infos demande AJAX */
        .demande-info {
            display:none; background:#eaf4fb; border:1px solid #aed6f1; border-radius:7px;
            padding:14px 16px; margin-top:10px; font-size:13px;
        }
        .demande-info.show { display:flex; gap:30px; flex-wrap:wrap; }
        .demande-info .info-col .label { font-size:11px; text-transform:uppercase; color:#7f8c8d; font-weight:600; }
        .demande-info .info-col .val { font-size:14px; color:#2c3e50; font-weight:600; margin-top:2px; }
        .demande-info-err { display:none; color:#e74c3c; font-size:13px; margin-top:6px; }
        .demande-info-err.show { display:block; }

        /* Ligne de détail inline */
        .detail-form-row {
            display:grid;
            grid-template-columns: 2.5fr 1fr 1fr 1.5fr 1.5fr auto;
            gap:10px;
            align-items:end;
            background:#f8f9fa;
            border:1.5px dashed #dfe6e9;
            border-radius:8px;
            padding:16px;
            margin-bottom:14px;
        }
        .detail-form-row .form-group { margin-bottom:0; }

        /* Tableau preview des lignes */
        .details-preview { overflow-x:auto; }
        table { width:100%; border-collapse:collapse; }
        thead th {
            background:#2c3e50; color:#fff; padding:10px 13px; text-align:left;
            font-size:12px; font-weight:600; text-transform:uppercase; letter-spacing:.4px;
        }
        tbody tr { border-bottom:1px solid #ecf0f1; transition:background .15s; }
        tbody tr:hover { background:#f5fbff; }
        tbody td { padding:9px 13px; font-size:13px; }
        .text-right { text-align:right; }
        .text-center { text-align:center; }
        .montant-col { font-weight:700; color:#27ae60; }

        /* Boutons */
        .btn {
            display:inline-block; padding:9px 20px; border:none; border-radius:6px;
            font-size:13px; font-weight:600; cursor:pointer; text-decoration:none; transition:all .2s;
        }
        .btn-primary { background:#3498db; color:#fff; }
        .btn-primary:hover { background:#2980b9; transform:translateY(-1px); box-shadow:0 4px 12px rgba(52,152,219,.3); }
        .btn-success { background:#27ae60; color:#fff; }
        .btn-success:hover { background:#219a52; }
        .btn-secondary { background:#95a5a6; color:#fff; }
        .btn-secondary:hover { background:#7f8c8d; }
        .btn-danger-sm { background:#e74c3c; color:#fff; padding:5px 11px; font-size:12px; border:none; border-radius:4px; cursor:pointer; }
        .btn-danger-sm:hover { background:#c0392b; }
        .btn-add { background:#16a085; color:#fff; padding:10px 16px; white-space:nowrap; }
        .btn-add:hover { background:#138d75; }

        .form-actions { display:flex; gap:12px; margin-top:10px; }

        /* Total */
        .total-bar {
            display:flex; justify-content:flex-end; align-items:center;
            padding:14px 16px; background:#eafaf1; border-radius:8px;
            border:1px solid #a9dfbf; margin-top:14px; font-weight:700; font-size:16px; color:#1e8449;
        }

        /* Toast */
        .toast {
            display:none; position:fixed; bottom:30px; right:30px; z-index:999;
            padding:14px 22px; border-radius:8px; font-size:14px; font-weight:600;
            box-shadow:0 4px 20px rgba(0,0,0,0.15); animation:slideIn .3s ease;
        }
        .toast.success { background:#27ae60; color:#fff; display:block; }
        .toast.error   { background:#e74c3c; color:#fff; display:block; }
        @keyframes slideIn { from { transform:translateY(30px); opacity:0; } to { transform:translateY(0); opacity:1; } }

        .spinner { display:none; width:18px; height:18px; border:3px solid rgba(255,255,255,.4); border-top-color:#fff; border-radius:50%; animation:spin .6s linear infinite; vertical-align:middle; margin-left:8px; }
        @keyframes spin { to { transform:rotate(360deg); } }
        button:disabled .spinner { display:inline-block; }
        button:disabled { opacity:.75; cursor:not-allowed; }

        .empty-details { text-align:center; padding:22px; color:#aaa; font-size:13px; font-style:italic; }
    </style>
</head>
<body>
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/" class="brand">GestionForage</a>
        <ul class="nav-links">
            <li><a href="${pageContext.request.contextPath}/demandes">Demandes</a></li>
            <li><a href="${pageContext.request.contextPath}/devis" class="active">Devis</a></li>
            <li><a href="${pageContext.request.contextPath}/status-demandes">Status Demande</a></li>
        </ul>
    </nav>

    <div class="container">
        <h1>Nouveau Devis</h1>

        <!-- ===== SECTION 1 : Infos générales ===== -->
        <div class="card">
            <h2>Informations générales</h2>

            <div class="form-group">
                <label for="demandeRef" class="required">Référence demande</label>
                <input type="text" id="demandeRef" placeholder="Ex: DEM-2026-001"
                       autocomplete="off" onblur="fetchDemandeInfo()" />
                <!-- Infos retournées par AJAX -->
                <div class="demande-info" id="demandeInfo">
                    <div class="info-col">
                        <div class="label">Client</div>
                        <div class="val" id="info-client">—</div>
                    </div>
                    <div class="info-col">
                        <div class="label">Lieu de forage</div>
                        <div class="val" id="info-lieu">—</div>
                    </div>
                    <div class="info-col">
                        <div class="label">Date demande</div>
                        <div class="val" id="info-date">—</div>
                    </div>
                    <div class="info-col">
                        <div class="label">Commune</div>
                        <div class="val" id="info-commune">—</div>
                    </div>
                </div>
                <div class="demande-info-err" id="demandeInfoErr">Demande introuvable.</div>
            </div>

            <div class="form-group">
                <label for="typeDevis" class="required">Type de devis</label>
                <select id="typeDevis">
                    <option value="">-- Choisir un type --</option>
                    <c:forEach items="${types}" var="t">
                        <option value="${t.id}">${t.libele}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="dateDevis" class="required">Type de devis</label>
                <input type="datetime-local" name="dateDevis" id="dateDevis>
            </div>

            
            <div class="form-group">
                <label for="observation">Observation</label>
                <textarea id="observation" placeholder="Remarques, conditions particulières..."></textarea>
            </div>
        </div>

        <!-- ===== SECTION 2 : Lignes de détail ===== -->
        <div class="card">
            <h2>Lignes de détail</h2>

            <!-- Formulaire inline d'ajout -->
            <div class="detail-form-row">
                <div class="form-group">
                    <label for="d-designation">Désignation</label>
                    <input type="text" id="d-designation" placeholder="Ex: Tube PVC 110mm" />
                </div>
                <div class="form-group">
                    <label for="d-qte">Qté</label>
                    <input type="number" id="d-qte" placeholder="0" min="0" step="0.01"
                           oninput="calcMontantPreview()" />
                </div>
                <div class="form-group">
                    <label for="d-unite">Unité</label>
                    <input type="text" id="d-unite" placeholder="m, kg, pcs" />
                </div>
                <div class="form-group">
                    <label for="d-pu">Prix unitaire</label>
                    <input type="number" id="d-pu" placeholder="0.00" min="0" step="0.01"
                           oninput="calcMontantPreview()" />
                </div>
                <div class="form-group">
                    <label for="d-montant">Montant</label>
                    <input type="text" id="d-montant" placeholder="0.00" readonly
                           style="background:#f0f0f0; color:#27ae60; font-weight:700;" />
                </div>
                <div class="form-group">
                    <label>&nbsp;</label>
                    <button type="button" class="btn btn-add" onclick="ajouterLigne()">+ Ajouter</button>
                </div>
            </div>

            <!-- Tableau preview des lignes ajoutées -->
            <div class="details-preview">
                <table id="detailsTable">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Désignation</th>
                            <th>Qté</th>
                            <th>Unité</th>
                            <th>Prix unitaire</th>
                            <th>Montant</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody id="detailsTbody">
                        <tr id="emptyRow">
                            <td colspan="7" class="empty-details">Aucune ligne ajoutée. Remplissez le formulaire ci-dessus.</td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- Barre total -->
            <div class="total-bar">
                MONTANT TOTAL : <span id="totalDisplay" style="margin-left:10px;">0.00 Ar</span>
            </div>
        </div>

        <!-- ===== BOUTONS FINAUX ===== -->
        <div class="form-actions">
            <button type="button" class="btn btn-success" id="btnEnregistrer" onclick="enregistrerDevis()">
                ✔ Enregistrer le devis
                <span class="spinner" id="spinner"></span>
            </button>
            <a href="${pageContext.request.contextPath}/devis" class="btn btn-secondary">Annuler</a>
        </div>
    </div>

    <!-- Toast notification -->
    <div class="toast" id="toast"></div>

    <script>
        const CTX = '${pageContext.request.contextPath}';
        let lignes = [];    // tableau JS des lignes de détail
        let total  = 0.0;

        // ─── AJAX : récupérer infos demande au onblur ───────────────────────
        function fetchDemandeInfo() {
            const ref = document.getElementById('demandeRef').value.trim();
            const infoDiv = document.getElementById('demandeInfo');
            const errDiv  = document.getElementById('demandeInfoErr');

            infoDiv.classList.remove('show');
            errDiv.classList.remove('show');

            if (!ref) return;

            fetch(CTX + '/devis/ajax/demande/' + encodeURIComponent(ref))
                .then(r => r.json())
                .then(data => {
                    if (data.error) {
                        errDiv.textContent = data.error;
                        errDiv.classList.add('show');
                        return;
                    }
                    document.getElementById('info-client').textContent  = data.client  || '—';
                    document.getElementById('info-lieu').textContent    = data.lieuForage || '—';
                    document.getElementById('info-commune').textContent = data.commune  || '—';

                    // Formater la date
                    if (data.dateDemande) {
                        const d = new Date(data.dateDemande);
                        document.getElementById('info-date').textContent =
                            d.toLocaleDateString('fr-FR') + ' ' + d.toLocaleTimeString('fr-FR', {hour:'2-digit', minute:'2-digit'});
                    } else {
                        document.getElementById('info-date').textContent = '—';
                    }
                    infoDiv.classList.add('show');
                })
                .catch(() => {
                    errDiv.textContent = 'Erreur de connexion au serveur.';
                    errDiv.classList.add('show');
                });
        }

        // ─── Calcul montant preview dans le formulaire d'ajout ──────────────
        function calcMontantPreview() {
            const qte = parseFloat(document.getElementById('d-qte').value) || 0;
            const pu  = parseFloat(document.getElementById('d-pu').value)  || 0;
            document.getElementById('d-montant').value = (qte * pu).toFixed(2);
        }

        // ─── Ajouter une ligne au tableau ────────────────────────────────────
        function ajouterLigne() {
            const designation = document.getElementById('d-designation').value.trim();
            const qte   = parseFloat(document.getElementById('d-qte').value);
            const unite = document.getElementById('d-unite').value.trim();
            const pu    = parseFloat(document.getElementById('d-pu').value);

            if (!designation) { showToast('La désignation est obligatoire.', 'error'); return; }
            if (!qte || qte <= 0) { showToast('La quantité doit être > 0.', 'error'); return; }
            if (!pu  || pu  <= 0) { showToast('Le prix unitaire doit être > 0.', 'error'); return; }

            const montant = qte * pu;
            lignes.push({ designation, quantite: qte, unite, prixUnitaire: pu, montantParLigne: montant });

            renderTable();
            clearDetailForm();
        }

        // ─── Supprimer une ligne ─────────────────────────────────────────────
        function supprimerLigne(idx) {
            lignes.splice(idx, 1);
            renderTable();
        }

        // ─── Afficher le tableau ─────────────────────────────────────────────
        function renderTable() {
            const tbody = document.getElementById('detailsTbody');
            tbody.innerHTML = '';

            if (lignes.length === 0) {
                tbody.innerHTML = '<tr id="emptyRow"><td colspan="7" class="empty-details">Aucune ligne ajoutée. Remplissez le formulaire ci-dessus.</td></tr>';
                total = 0;
            } else {
                total = 0;
                lignes.forEach((l, i) => {
                    total += l.montantParLigne;
                    let html = '<tr>';
                    html += '<td>' + (i + 1) + '</td>';
                    html += '<td>' + escHtml(l.designation) + '</td>';
                    html += '<td class="text-center">' + l.quantite.toFixed(2) + '</td>';
                    html += '<td class="text-center">' + escHtml(l.unite) + '</td>';
                    html += '<td class="text-right">' + l.prixUnitaire.toFixed(2) + '</td>';
                    html += '<td class="text-right montant-col">' + l.montantParLigne.toFixed(2) + '</td>';
                    html += '<td class="text-center">';
                    html += '<button type="button" class="btn-danger-sm" onclick="supprimerLigne(' + i + ')">✕</button>';
                    html += '</td>';
                    html += '</tr>';
                    tbody.innerHTML += html;
                });
            }

            document.getElementById('totalDisplay').textContent =
                new Intl.NumberFormat('fr-FR', {minimumFractionDigits:2}).format(total) + ' Ar';
        }

        // ─── Vider le formulaire inline ──────────────────────────────────────
        function clearDetailForm() {
            ['d-designation','d-qte','d-unite','d-pu','d-montant'].forEach(id => {
                document.getElementById(id).value = '';
            });
        }

        // ─── Enregistrement final (JSON POST) ────────────────────────────────
        function enregistrerDevis() {
            const ref    = document.getElementById('demandeRef').value.trim();
            const typeId = document.getElementById('typeDevis').value;
            const obs    = document.getElementById('observation').value.trim();

            if (!ref)    { showToast('La référence de la demande est obligatoire.', 'error'); return; }
            if (!typeId) { showToast('Veuillez choisir un type de devis.', 'error'); return; }
            if (lignes.length === 0) { showToast('Ajoutez au moins une ligne de détail.', 'error'); return; }

            const payload = {
                demandeReference: ref,
                typeId: parseInt(typeId),
                observation: obs,
                details: lignes.map(l => ({
                    designation:   l.designation,
                    quantite:      l.quantite,
                    unite:         l.unite,
                    prixUnitaire:  l.prixUnitaire
                }))
            };

            const btn = document.getElementById('btnEnregistrer');
            btn.disabled = true;
            document.getElementById('spinner').style.display = 'inline-block';

            fetch(CTX + '/devis/creer', {
                method:  'POST',
                headers: { 'Content-Type': 'application/json' },
                body:    JSON.stringify(payload)
            })
            .then(r => r.json())
            .then(data => {
                if (data.success) {
                    showToast('Devis créé avec succès ! ID: ' + data.devisId, 'success');
                    setTimeout(() => { window.location.href = CTX + '/devis/' + data.devisId; }, 1500);
                } else {
                    showToast(data.error || 'Erreur lors de la création.', 'error');
                    btn.disabled = false;
                    document.getElementById('spinner').style.display = 'none';
                }
            })
            .catch(() => {
                showToast('Erreur de connexion au serveur.', 'error');
                btn.disabled = false;
                document.getElementById('spinner').style.display = 'none';
            });
        }

        // ─── Helpers ─────────────────────────────────────────────────────────
        function showToast(msg, type) {
            const t = document.getElementById('toast');
            t.textContent = msg;
            t.className = 'toast ' + type;
            setTimeout(() => { t.className = 'toast'; }, 4000);
        }

        function escHtml(str) {
            return str.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
        }
    </script>
</body>
</html>
