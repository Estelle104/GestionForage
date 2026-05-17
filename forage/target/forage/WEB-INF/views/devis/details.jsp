<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Devis_${devis.id} - Details - GestionForage</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f0f2f5;
            color: #2c3e50;
            min-height: 100vh;
        }
        .container {
            max-width: 1100px;
            margin: 0 auto;
            padding: 30px 20px;
        }
        h1 {
            font-size: 28px;
            color: #2c3e50;
            margin-bottom: 25px;
            padding-bottom: 12px;
            border-bottom: 3px solid #3498db;
        }
        h2 {
            font-size: 20px;
            color: #34495e;
            margin-bottom: 15px;
            margin-top: 30px;
        }
        .card {
            background: #fff;
            border-radius: 10px;
            padding: 25px;
            box-shadow: 0 2px 15px rgba(0,0,0,0.08);
            margin-bottom: 25px;
        }
        .info-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
        }
        .info-item {
            padding: 10px 0;
        }
        .info-item .label {
            font-size: 12px;
            color: #95a5a6;
            text-transform: uppercase;
            font-weight: 600;
            letter-spacing: 0.5px;
        }
        .info-item .value {
            font-size: 16px;
            color: #2c3e50;
            margin-top: 4px;
            font-weight: 500;
        }
        .montant-total {
            font-size: 24px;
            font-weight: 700;
            color: #27ae60;
        }
        .status-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            text-transform: uppercase;
        }
        .status-DEVIS_FORAGE_CREE { background: #1565c0; color: #fff; }
        .status-DEVIS_ETUDE_CREE  { background: #6a1b9a; color: #fff; }
        .status-DEVIS_ACCEPTE     { background: #27ae60; color: #fff; }
        .status-DEVIS_REFUSE      { background: #e74c3c; color: #fff; }
        .type-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 700;
            text-transform: uppercase;
            background: #37474f;
            color: #fff;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            background: #fff;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 15px rgba(0,0,0,0.08);
        }
        thead th {
            background: #2c3e50;
            color: #fff;
            padding: 12px 14px;
            text-align: left;
            font-size: 13px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        tbody tr {
            border-bottom: 1px solid #ecf0f1;
        }
        tbody tr:hover {
            background: #ebf5fb;
        }
        tbody td {
            padding: 10px 14px;
            font-size: 14px;
        }
        .text-right {
            text-align: right;
        }
        .text-center {
            text-align: center;
        }
        /* Formulaire ajout detail */
        .form-inline {
            display: grid;
            grid-template-columns: 2fr 1fr 1fr 1.5fr 1.5fr auto;
            gap: 10px;
            align-items: end;
            margin-top: 20px;
        }
        .form-group {
            display: flex;
            flex-direction: column;
        }
        .form-group label {
            font-size: 12px;
            font-weight: 600;
            color: #34495e;
            margin-bottom: 4px;
        }
        .form-group input,
        .form-group select {
            padding: 8px 12px;
            border: 2px solid #dfe6e9;
            border-radius: 6px;
            font-size: 13px;
            transition: border-color 0.2s ease;
        }
        .form-group input:focus,
        .form-group select:focus {
            outline: none;
            border-color: #3498db;
            box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
        }
        .btn {
            display: inline-block;
            padding: 10px 22px;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            font-weight: 600;
            text-decoration: none;
            cursor: pointer;
            transition: all 0.2s ease;
        }
        .btn-primary {
            background: #3498db;
            color: #fff;
        }
        .btn-primary:hover {
            background: #2980b9;
            transform: translateY(-1px);
            box-shadow: 0 4px 12px rgba(52, 152, 219, 0.3);
        }
        .btn-success {
            background: #27ae60;
            color: #fff;
        }
        .btn-success:hover {
            background: #219a52;
        }
        .btn-danger {
            background: #e74c3c;
            color: #fff;
            padding: 8px 16px;
            font-size: 13px;
        }
        .btn-danger:hover {
            background: #c0392b;
        }
        .btn-secondary {
            background: #95a5a6;
            color: #fff;
        }
        .btn-secondary:hover {
            background: #7f8c8d;
        }
        .top-actions {
            display: flex;
            gap: 12px;
            margin-bottom: 20px;
        }
        .empty-details {
            text-align: center;
            padding: 30px;
            color: #95a5a6;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Devis_${devis.id}</h1>

        <div class="top-actions">
            <a href="${pageContext.request.contextPath}/devis" class="btn btn-secondary">&#8592; Retour a la liste</a>
            <a href="${pageContext.request.contextPath}/devis/${devis.id}/pdf" class="btn btn-danger">Exporter PDF</a>
        </div>

        <!-- Informations du devis -->
        <div class="card">
            <div class="info-grid">
                <div class="info-item">
                    <div class="label">Reference Demande</div>
                    <div class="value">${devis.demande.reference}</div>
                </div>
                <div class="info-item">
                    <div class="label">Client</div>
                    <div class="value">${devis.demande.client.nom}</div>
                </div>
                <div class="info-item">
                    <div class="label">Date du devis</div>
                    <div class="value">
                        <fmt:formatDate value="${devis.dateDevis}" pattern="dd/MM/yyyy HH:mm"/>
                    </div>
                </div>
                <div class="info-item">
                    <div class="label">Type</div>
                    <div class="value">
                        <span class="type-badge">${devis.type != null ? devis.type.libele : 'N/A'}</span>
                    </div>
                </div>
                <div class="info-item">
                    <div class="label">Montant Total</div>
                    <div class="value montant-total">
                        <fmt:formatNumber value="${devis.montantDevis}" type="number" minFractionDigits="2" maxFractionDigits="2"/> Ar
                    </div>
                </div>
                <div class="info-item">
                    <div class="label">Lieu Forage</div>
                    <div class="value">${devis.demande.lieuForage}</div>
                </div>
            </div>
        </div>

        <!-- Tableau des details -->
        <h2>Lignes de detail</h2>

        <c:choose>
            <c:when test="${not empty detailsList}">
                <table>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Designation</th>
                            <th>Quantite</th>
                            <th>Unite</th>
                            <th>Prix Unitaire</th>
                            <th>Montant</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${detailsList}" var="det" varStatus="loop">
                            <tr>
                                <td>${loop.index + 1}</td>
                                <td>${det.designation}</td>
                                <td class="text-center">
                                    <fmt:formatNumber value="${det.quantite}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                                </td>
                                <td class="text-center">${det.unite}</td>
                                <td class="text-right">
                                    <fmt:formatNumber value="${det.prixUnitaire}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                                </td>
                                <td class="text-right" style="font-weight:700;color:#27ae60;">
                                    <fmt:formatNumber value="${det.montantParLigne}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="card">
                    <div class="empty-details">Aucun detail pour ce devis. Ajoutez des lignes ci-dessous.</div>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</body>
</html>
