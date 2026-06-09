<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Devis - GestionForage</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f5f5;
            color: #333;
            min-height: 100vh;
        }

        /* Navigation */
        .navbar {
            background: #2d2d2d;
            padding: 0 30px;
            display: flex;
            align-items: center;
            height: 56px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.15);
        }
        .navbar .brand {
            color: #fff;
            font-size: 18px;
            font-weight: 700;
            text-decoration: none;
            margin-right: 40px;
            letter-spacing: 0.5px;
        }
        .navbar .nav-links {
            display: flex;
            gap: 0;
            list-style: none;
        }
        .navbar .nav-links a {
            color: #ccc;
            text-decoration: none;
            padding: 16px 20px;
            font-size: 14px;
            font-weight: 500;
            transition: all 0.2s ease;
            border-bottom: 3px solid transparent;
        }
        .navbar .nav-links a:hover {
            color: #fff;
            background: rgba(255,255,255,0.05);
        }
        .navbar .nav-links a.active {
            color: #fff;
            border-bottom-color: #f0c040;
        }

        .container {
            max-width: 1100px;
            margin: 0 auto;
            padding: 30px 20px;
        }
        h1 {
            font-size: 26px;
            color: #2d2d2d;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #ddd;
        }
        .actions-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        .actions-bar span {
            color: #777;
            font-size: 14px;
        }

        /* Boutons */
        .btn {
            display: inline-block;
            padding: 9px 20px;
            border: none;
            border-radius: 5px;
            font-size: 13px;
            font-weight: 600;
            text-decoration: none;
            cursor: pointer;
            transition: all 0.2s ease;
        }
        .btn-green {
            background: #2e7d32;
            color: #fff;
        }
        .btn-green:hover {
            background: #1b5e20;
            box-shadow: 0 2px 8px rgba(46,125,50,0.3);
        }
        .btn-red {
            background: #c62828;
            color: #fff;
            padding: 6px 14px;
            font-size: 12px;
        }
        .btn-red:hover {
            background: #b71c1c;
        }
        .btn-yellow {
            background: #f9a825;
            color: #333;
            padding: 6px 14px;
            font-size: 12px;
        }
        .btn-yellow:hover {
            background: #f57f17;
        }

        /* Tableau */
        table {
            width: 100%;
            border-collapse: collapse;
            background: #fff;
            border-radius: 6px;
            overflow: hidden;
            box-shadow: 0 1px 6px rgba(0,0,0,0.08);
        }
        thead th {
            background: #3a3a3a;
            color: #fff;
            padding: 12px 14px;
            text-align: left;
            font-size: 12px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        tbody tr {
            border-bottom: 1px solid #eee;
            transition: background 0.15s ease;
        }
        tbody tr:hover {
            background: #f9f9f9;
        }
        tbody td {
            padding: 11px 14px;
            font-size: 14px;
            color: #444;
        }
        .clickable-row {
            cursor: pointer;
        }
        .status-badge {
            display: inline-block;
            padding: 3px 10px;
            border-radius: 12px;
            font-size: 11px;
            font-weight: 600;
            text-transform: uppercase;
        }
        .status-DEVIS_FORAGE_CREE { background: #1565c0; color: #fff; }
        .status-DEVIS_ETUDE_CREE  { background: #6a1b9a; color: #fff; }
        .status-DEVIS_ACCEPTE     { background: #2e7d32; color: #fff; }
        .status-DEVIS_REFUSE      { background: #c62828; color: #fff; }
        .type-badge {
            display: inline-block;
            padding: 3px 10px;
            border-radius: 12px;
            font-size: 11px;
            font-weight: 600;
            text-transform: uppercase;
            background: #37474f;
            color: #fff;
        }
        .montant {
            font-weight: 700;
            color: #2e7d32;
        }
        .actions-cell {
            display: flex;
            gap: 6px;
            align-items: center;
        }
        .empty-state {
            text-align: center;
            padding: 50px 20px;
            color: #999;
            background: #fff;
            border-radius: 6px;
            box-shadow: 0 1px 6px rgba(0,0,0,0.08);
        }
        .empty-state p {
            font-size: 15px;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/" class="brand">GestionForage - ETU004185</a>
        <ul class="nav-links">
            <li><a href="${pageContext.request.contextPath}/demandes">Demandes</a></li>
            <li><a href="${pageContext.request.contextPath}/devis" class="active">Devis</a></li>
            <li><a href="${pageContext.request.contextPath}/status-demandes">Status Demande</a></li>
        </ul>
    </nav>

    <div class="container">
        <h1>Liste des Devis</h1>

        <div class="actions-bar">
            <span>${devisList.size()} devis</span>
            <a href="${pageContext.request.contextPath}/devis/new" class="btn btn-green">+ Nouveau Devis</a>
        </div>

        <c:choose>
            <c:when test="${not empty devisList}">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Ref. Demande</th>
                            <th>Client</th>
                            <th>Montant (Ar)</th>
                            <th>Date</th>
                            <th>Type</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${devisList}" var="d">
                            <tr class="clickable-row" onclick="window.location='${pageContext.request.contextPath}/devis/${d.id}'">
                                <td>${d.id}</td>
                                <td>${d.demande.reference}</td>
                                <td>${d.demande.client.nom}</td>
                                <td class="montant">
                                    <fmt:formatNumber value="${d.montantDevis}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                                </td>
                                <td>
                                    <fmt:formatDate value="${d.dateDevis}" pattern="dd/MM/yyyy HH:mm"/>
                                </td>
                                <td>
                                    <span class="type-badge">${d.type != null ? d.type.libele : 'N/A'}</span>
                                </td>
                                <td onclick="event.stopPropagation();">
                                    <div class="actions-cell">
                                        <a href="${pageContext.request.contextPath}/devis/${d.id}" class="btn btn-yellow">Details</a>
                                        <a href="${pageContext.request.contextPath}/devis/${d.id}/pdf" class="btn btn-red">PDF</a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <p>Aucun devis pour le moment.</p>
                    <a href="${pageContext.request.contextPath}/devis/new" class="btn btn-green">Creer votre premier devis</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
