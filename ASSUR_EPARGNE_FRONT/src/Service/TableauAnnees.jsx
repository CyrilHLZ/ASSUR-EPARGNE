const euros = (v) => Math.round(v).toLocaleString('fr-FR') + ' €';
const pct = (v) => (Math.round(v * 10000) / 100).toLocaleString('fr-FR') + ' %';

export default function TableauAnnees({ lignes, cotisation }) {
  return (
    <section className="panneau">
      <h2>Détail année par année</h2>
      <div className="detail">
        <table>
          <thead>
            <tr>
              <th>Année</th>
              <th>Taux du bonus</th>
              <th>Cotisations</th>
              <th>Bonus de l'année</th>
              <th>Capital</th>
            </tr>
          </thead>
          <tbody>
            {lignes.map((ligne) => {
              const contrats = [ligne.sinistreAuto && 'auto', ligne.sinistreHabitation && 'habitation'].filter(Boolean);
              return (
                <tr key={ligne.annee}>
                  <td>{ligne.annee}</td>
                  <td>
                    {contrats.length > 0 ? (
                      <>
                        <span className="zero">0 %</span>
                        <span className="pilule">sinistre {contrats.join(' et ')}</span>
                      </>
                    ) : (
                      pct(ligne.taux)
                    )}
                  </td>
                  <td>{euros(cotisation)}</td>
                  <td>{euros(ligne.bonus)}</td>
                  <td>{euros(ligne.capital)}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </section>
  );
}
