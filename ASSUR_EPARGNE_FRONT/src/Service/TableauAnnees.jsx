const euros = (v) => Math.round(v).toLocaleString('fr-FR') + ' €';
const pct = (v) => (Math.round(v * 10000) / 100).toLocaleString('fr-FR') + ' %';

export default function TableauAnnees({ lignes }) {
  return (
    <section className="panneau">
      <h2>Détail année par année</h2>
      <div className="detail">
        <table>
          <thead>
            <tr>
              <th>Année</th>
              <th>Taux auto</th>
              <th>Taux habitation</th>
              <th>Cotisations</th>
              <th>Bonus de l'année</th>
              <th>Capital</th>
            </tr>
          </thead>
          <tbody>
            {lignes.map((ligne) => (
              <tr key={ligne.annee}>
                <td>{ligne.annee}</td>
                <td>
                  {ligne.sinistreAuto
                    ? <><span className="zero">0 %</span><span className="pilule">sinistre</span></>
                    : pct(ligne.tauxAuto)}
                </td>
                <td>
                  {ligne.sinistreHabitation
                    ? <><span className="zero">0 %</span><span className="pilule">sinistre</span></>
                    : pct(ligne.tauxHabitation)}
                </td>
                <td>{euros(ligne.cotisationAuto + ligne.cotisationHabitation)}</td>
                <td>{euros(ligne.bonus)}</td>
                <td>{euros(ligne.capital)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
