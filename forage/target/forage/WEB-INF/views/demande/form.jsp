<form action="${pageContext.request.contextPath}/demandes/save" method="post">

    <label for="reference">Reference:</label>
    <input type="text" name="reference" /><br>

    <label for="nomDemandeur">Nom:</label>
    <input type="text" name="nomDemandeur" /><br>

    <label for="lieuForage">Lieu:</label>
    <input type="text" name="lieuForage" /><br>

    <button type="submit">Demander un forage</button>

</form>