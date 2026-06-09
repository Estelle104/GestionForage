<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Liste des Statuts de Demandes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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
            max-width: 1400px;
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

        /* Tableau */
        table {
            width: 100%;
            border-collapse: collapse;
            background: #fff;
            border-radius: 6px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
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

        /* Lignes de groupe (demande) */
        tr.demande-group {
            background: #f0f0f0;
            font-weight: 700;
            border-bottom: 2px solid #ddd;
        }

        tr.demande-group td {
            padding: 12px 14px;
            font-size: 14px;
            color: #2d2d2d;
        }

        /* Lignes de détail (status) */
        tr.status-detail {
            border-bottom: 1px solid #eee;
            background: #fff;
        }

        tr.status-detail:hover {
            background: #f9f9f9;
        }

        tr.status-detail td {
            padding: 10px 14px;
            font-size: 13px;
            color: #555;
            padding-left: 40px;
        }

        /* Colonne indentation */
        td.indent {
            width: 30px;
            padding: 10px 5px !important;
            text-align: center;
        }

        /* Alerte Badge */
        .alerte-badge {
            display: inline-block;
            padding: 4px 8px;
            border-radius: 3px;
            font-size: 11px;
            font-weight: 600;
            margin-right: 5px;
            margin-bottom: 3px;
            white-space: nowrap;
        }

        .alerte-active {
            border: 1px solid;
            color: white;
        }

        .alerte-badge-vert {
            background: #d4edda;
            color: #155724;
            border-color: #c3e6cb;
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

        .status-label {
            display: inline-block;
            padding: 4px 10px;
            background: #e3f2fd;
            border-radius: 3px;
            font-size: 12px;
            font-weight: 600;
            color: #1976d2;
        }

        .client-info {
            font-size: 13px;
            color: #666;
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/" class="brand">GestionForage</a>
        <ul class="nav-links">
            <li><a href="${pageContext.request.contextPath}/demandes">Demandes</a></li>
            <li><a href="${pageContext.request.contextPath}/devis">Devis</a></li>
            <li><a href="${pageContext.request.contextPath}/status-demandes" class="active">Statuts Demandes</a></li>
        </ul>
    </nav>

    <div class="container">
        <h1>Historique des Statuts de Demandes</h1>
        <div class="actions-bar">
            <a href="${pageContext.request.contextPath}/status-demandes/create" class="btn btn-green">+ Nouveau Statut</a>
        </div>

        <c:if test="${empty demandes}">
            <div class="empty-state">
                <p>Aucune demande trouvée</p>
            </div>
        </c:if>

        <c:if test="${not empty demandes}">
            <table>
                <thead>
                    <tr>
                        <th style="width: 100px;">Demande #</th>
                        <th style="width: 150px;">Référence</th>
                        <th style="width: 200px;">Client & Commune</th>
                        <th style="width: 150px;">Date</th>
                        <th style="width: 180px;">Statut</th>
                        <th style="flex: 1;">Alertes</th>
                        <th style="width: 150px;">Observation</th>
                        <th style="width: 120px;">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="demande" items="${demandes}">
                        <c:if test="${not empty statusParDemande[demande.id]}">
                            <!-- Ligne de groupe (Demande) -->
                            <tr class="demande-group">
                                <td colspan="8">
                                    <strong>Demande #${demande.id} - ${demande.reference}</strong>
                                    <span class="client-info" style="margin-left: 15px;">
                                        👤 ${demande.client.nom} | 📍 ${demande.commune.nom}
                                    </span>
                                    <!-- DEBUG: afficher nombre de statuts reçus -->
                                    <span style="margin-left:20px; font-weight:600; color:#666; font-size:12px;">(
                                        <c:out value="${fn:length(statusParDemande[demande.id])}"></c:out> statuts)</span>
                                </td>
                            </tr>

                            <!-- Lignes de détail (Statuts) -->
                            <c:forEach var="status" items="${statusParDemande[demande.id]}" varStatus="loop">
                                <tr class="status-detail">
                                    <td class="indent">
                                        <c:if test="${loop.first}">
                                            ↳
                                        </c:if>
                                    </td>
                                    <td></td>
                                    <td></td>
                                    <!-- <td>
                                        <fmt:formatDate value="${status.dateStatus}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                    </td> -->
                                    <td>
                                        ${status.dateStatus}
                                    </td>
                                    <td>
                                        <span class="status-label">${status.statusLibele}</span>
                                    </td>
                                    <td>
                                       <c:if test="${empty status.alertes}">
        <span style="color:#999;">—</span>
    </c:if>

    <c:if test="${not empty status.alertes}">
        <c:forEach var="alerte" items="${status.alertes}">

    <c:choose>

        <c:when test="${alerte.isAlerte}">
            <div class="alerte-badge alerte-active"
                 style="background-color:${alerte.couleur};
                        border-color:${alerte.couleur};
                        color:#fff;"
                 title="${alerte.description}">

                ⚠ ${alerte.statusLabel}

                <br/>

                <span style="font-size:10px;">
                    ${alerte.dureeActuelle} / ${alerte.dureeMinute} min
                </span>

            </div>
        </c:when>

        <c:otherwise>
            <div class="alerte-badge alerte-badge-vert"
                 title="${alerte.description}">

                ${alerte.statusLabel}

                <br/>

                <span style="font-size:10px;">
                    ${alerte.dureeActuelle} / ${alerte.dureeMinute} min
                </span>

            </div>
        </c:otherwise>

    </c:choose>

</c:forEach>
    </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${not empty status.observation}">
                                            <span title="${status.observation}" style="color: #666; font-size: 12px;">
                                                📝 ${status.observation}
                                            </span>
                                        </c:if>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/status-demandes/edit/${status.id}" class="btn" style="padding:6px 10px; font-size:12px; background:#f0f0f0; border:1px solid #ddd; text-decoration:none; color:#333; border-radius:4px;">Modifier</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
</body>
</html>

