const CONTRATS = [
  { cle: 'auto', libelle: 'Auto' },
  { cle: 'habitation', libelle: 'Habitation' },
];

export default function GrilleSinistres({ duree, sinistresAuto, sinistresHabitation, onBasculer, onAucun, onProfilMoyen }) {
  const annees = Array.from({ length: duree }, (_, i) => i + 1);
  const listes = { auto: sinistresAuto, habitation: sinistresHabitation };

  return (
    <section className="panneau">
      <h2>Vos sinistres responsables</h2>
      <p className="aide">Cliquez sur une année pour y placer un sinistre (rouge).</p>

      <div className="sinistres">
        <table>
          <thead>
            <tr>
              <th></th>
              {annees.map((annee) => <th key={annee} className="an">{annee}</th>)}
            </tr>
          </thead>
          <tbody>
            {CONTRATS.map(({ cle, libelle }) => (
              <tr key={cle}>
                <th scope="row">{libelle}</th>
                {annees.map((annee) => {
                  const sinistre = listes[cle].includes(annee);
                  return (
                    <td key={annee}>
                      <button
                        type="button"
                        className={sinistre ? 'case sinistre' : 'case'}
                        aria-pressed={sinistre}
                        aria-label={`${libelle}, année ${annee} : ${sinistre ? 'sinistre' : 'sans sinistre'}`}
                        onClick={() => onBasculer(cle, annee)}
                      >
                        {sinistre ? '!' : ''}
                      </button>
                    </td>
                  );
                })}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="actions">
        <button type="button" className="btn" onClick={onAucun}>Aucun sinistre</button>
        <button type="button" className="btn" onClick={onProfilMoyen}>Profil moyen français</button>
      </div>
      <p className="aide">
        Profil moyen : environ 1 sinistre habitation tous les 11 ans et 1 sinistre auto responsable tous les 34 ans,
        d'après les fréquences publiées par France Assureurs.
      </p>
    </section>
  );
}
