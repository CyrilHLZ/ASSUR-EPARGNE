const euros = new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'EUR' });

export default function TableauAnnees({ lignes }) {
  return (
    <div className="carte">
      <h2>Détail par année</h2>
      <div className="tableau-conteneur">
        <table className="tableau">
          <thead>
            <tr>
              <th>Année</th>
              <th>Capital</th>
              <th>Bonus</th>
            </tr>
          </thead>
          <tbody>
            {lignes.map((ligne) => (
              <tr key={ligne.annee}>
                <td>{ligne.annee}</td>
                <td>{euros.format(ligne.capital)}</td>
                <td>{euros.format(ligne.bonus)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
