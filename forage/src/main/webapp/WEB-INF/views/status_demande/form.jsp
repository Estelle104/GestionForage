<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>${statusDemande != null ? 'Modifier' : 'Ajouter'} Statut Demande</title>
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
            max-width: 620px;
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
        .card {
            background: #fff;
            border-radius: 6px;
            padding: 28px;
            box-shadow: 0 1px 6px rgba(0,0,0,0.08);
        }
        .form-group {
            margin-bottom: 18px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: 600;
            font-size: 13px;
            color: #444;
        }
        .form-group select,
        .form-group input {
            width: 100%;
            padding: 9px 12px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.2s ease;
            background: #fff;
            color: #333;
        }
        .form-group select:focus,
        .form-group input:focus {
            outline: none;
            border-color: #666;
            box-shadow: 0 0 0 2px rgba(100,100,100,0.1);
        }
        .required::after {
            content: " *";
            color: #c62828;
        }
        .form-actions {
            display: flex;
            gap: 12px;
            margin-top: 22px;
        }

        /* Boutons */
        .btn {
            display: inline-block;
            padding: 10px 22px;
            border: none;
            border-radius: 5px;
            font-size: 14px;
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
        .btn-grey {
            background: #777;
            color: #fff;
        }
        .btn-grey:hover {
            background: #555;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>${statusDemande != null ? 'Modifier le statut de la demande' : 'Ajouter un nouveau statut'}</h1>
        
        <form action="${pageContext.request.contextPath}/status-demandes/${statusDemande != null ? 'edit/' += statusDemande.id : 'create'}" method="post">
            <div class="form-group">
                <label for="demandeId">Demande :</label>
                <select id="demandeId" name="demandeId" required>
                    <option value="">-- Sélectionner une demande --</option>
                    <c:forEach var="demande" items="${demandes}">
                        <option value="${demande.id}" ${statusDemande != null && statusDemande.demande.id == demande.id ? 'selected' : ''}>
                            ${demande.reference} - ${demande.client.nom}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="statusId">Statut :</label>
                <select id="statusId" name="statusId" required>
                    <option value="">-- Sélectionner un statut --</option>
                    <c:forEach var="status" items="${statuses}">
                        <option value="${status.id}" ${statusDemande != null && statusDemande.status.id == status.id ? 'selected' : ''}>
                            ${status.libele}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="dateStatus">Date :</label>
                <!-- Formater la date en string iso pour le champ datetime-local s'il y a une valeur existante -->
                <c:set var="dateFormatted" value="" />
                <c:if test="${statusDemande != null && statusDemande.dateStatus != null}">
                    <fmt:formatDate value="${statusDemande.dateStatus}" pattern="yyyy-MM-dd'T'HH:mm" var="dateFormatted"/>
                </c:if>
                <input type="datetime-local" id="dateStatus" name="dateStatus" value="${dateFormatted}" required>
            </div>

            <div class="form-group">
                <label for="observation">Observation :</label>
                <textarea id="observation" name="observation" rows="4">${statusDemande != null ? statusDemande.observation : ''}</textarea>
            </div>

            <button type="submit" class="btn">Enregistrer</button>
            <a href="${pageContext.request.contextPath}/status-demandes" class="btn btn-cancel">Annuler</a>
        </form>
    </div>
</body>
</html>
