<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
        .status-badge {
            display: inline-block;
            padding: 3px 10px;
            border-radius: 12px;
            font-size: 11px;
            font-weight: 600;
            text-transform: uppercase;
        }
        .status-EN_ATTENTE { background: #f9a825; color: #333; }
        .status-EN_COURS { background: #555; color: #fff; }
        .status-VALIDEE { background: #2e7d32; color: #fff; }
        .status-REFUSEE { background: #c62828; color: #fff; }
        .status-TERMINEE { background: #666; color: #fff; }

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
    <div class="container">
        <ul class="nav-links">
            <li><a href="${pageContext.request.contextPath}/demandes">Demandes</a></li>
            <li><a href="${pageContext.request.contextPath}/devis">Devis</a></li>
            <li><a href="${pageContext.request.contextPath}/status-demandes" class="active">Statuts Demandes</a></li>
        </ul>

        <h1>Historique des Statuts de Demandes</h1>
        <a href="${pageContext.request.contextPath}/status-demandes/create" class="btn">Nouveau Statut Demande</a>
        
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Demande (Ref)</th>
                    <th>Date</th>
                    <th>Status</th>
                    <th>Observation</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="sd" items="${statusDemandes}">
                    <tr>
                        <td>${sd.id}</td>
                        <td>${sd.demande.reference}</td>
                        <td><fmt:formatDate value="${sd.dateStatus}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td>${sd.status.libele}</td>
                        <td>${sd.observation}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/status-demandes/edit/${sd.id}" class="btn">Modifier</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
