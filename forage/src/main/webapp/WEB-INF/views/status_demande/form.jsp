<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>${statusDemande != null ? 'Modifier' : 'Ajouter'} Statut Demande</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7f6; margin: 0; padding: 20px; }
        .container { max-width: 600px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        h1 { color: #333; margin-bottom: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; font-weight: bold; margin-bottom: 5px; color: #555; }
        input[type="text"], input[type="datetime-local"], select, textarea {
            width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; font-family: inherit;
        }
        .btn { display: inline-block; padding: 10px 15px; color: white; background-color: #28a745; text-decoration: none; border-radius: 5px; border: none; cursor: pointer; font-size: 16px; }
        .btn:hover { background-color: #218838; }
        .btn-cancel { background-color: #6c757d; }
        .btn-cancel:hover { background-color: #5a6268; }
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
