const euros = (v) => Math.round(v).toLocaleString('fr-FR') + ' €';

export default function Comparaison({ lignes, cotisation, duree }) {
  const derniere = lignes[lignes.length - 1];
  const paye = cotisation * duree;
  const recupere = derniere.bonusCumul;
  const part = Math.min(100, (recupere / paye) * 100);

  return (
    <section className="panneau">
      <h2>Avec ou sans Assur'Épargne</h2>
      <div className="comparaison">
        <div className="cmp-ligne">
          <div className="cmp-libelle">Assureur classique<small>cotisations sur {duree} ans</small></div>
          <div className="cmp-barre">
            <div className="paye" style={{ width: '100%' }}>{euros(paye)} payés · 0 € récupéré</div>
          </div>
        </div>
        <div className="cmp-ligne">
          <div className="cmp-libelle">Assur'Épargne<small>mêmes cotisations</small></div>
          <div className="cmp-barre">
            <div className="paye" style={{ width: `${100 - part}%` }}>{euros(paye - recupere)}</div>
            <div className="recupere" style={{ width: `${part}%` }}>{part >= 9 ? euros(recupere) : ''}</div>
          </div>
        </div>
        <p className="cmp-note">
          Pour le même prix, vous récupérez <strong>{euros(recupere)}</strong> en épargne, soit{' '}
          {part.toLocaleString('fr-FR', { maximumFractionDigits: 1 })} % de vos cotisations.
          {derniere.versementsCumul > 0
            ? <> Avec vos versements et les intérêts, votre épargne atteint <strong>{euros(derniere.capital)}</strong>.</>
            : <> Avec les intérêts, ils deviennent <strong>{euros(derniere.capital)}</strong>.</>}
        </p>
      </div>
    </section>
  );
}
