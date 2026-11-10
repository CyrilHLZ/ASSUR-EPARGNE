const euros = (v) => Math.round(v).toLocaleString('fr-FR') + ' €';

const pct = (v) => (Math.round(v * 1000) / 10).toLocaleString('fr-FR') + ' %';
const NOMS_SUPPORT = { ASSURANCE_VIE: 'assurance vie', PER: 'PER' };

export default function Tuiles({ lignes, cotisation, duree, rendement, versementMensuel, totaux }) {
  const derniere = lignes[lignes.length - 1];

  return (
    <section className="tuiles" aria-label="Résultats">
      <div className="tuile principale">
        <span className="k">Capital au terme</span>
        <span className="v">{euros(derniere.capital)}</span>
        <span className="s">{euros(totaux.capitalNet)} net · {duree} ans en {NOMS_SUPPORT[totaux.support]}</span>
      </div>

      <div className="tuile">
        <span className="k"><span className="pastille" style={{ background: 'var(--accent)' }} />Bonus versés</span>
        <span className="v">{euros(derniere.bonusCumul)}</span>
        <span className="s">soit {euros(derniere.bonusCumul / duree)} par an en moyenne</span>
      </div>

      <div className="tuile">
        <span className="k"><span className="pastille" style={{ background: 'var(--second)' }} />Vos versements</span>
        <span className="v">{euros(derniere.versementsCumul)}</span>
        <span className="s">{versementMensuel ? `${versementMensuel} € par mois` : 'aucun versement libre'}</span>
      </div>

      <div className="tuile">
        <span className="k"><span className="pastille" style={{ background: 'var(--troisieme)' }} />Intérêts</span>
        <span className="v">{euros(derniere.interets)}</span>
        <span className="s">
          bruts à {rendement.toLocaleString('fr-FR')} % · {euros(totaux.interetsNets)} nets
          après prélèvements ({pct(totaux.tauxPrelevementsSociaux)})
        </span>
      </div>

      <div className="tuile">
        <span className="k"><span className="pastille" style={{ background: 'var(--ligne)' }} />Cotisations payées</span>
        <span className="v">{euros(derniere.cotisationsCumul)}</span>
        <span className="s">{euros(cotisation)} la 1ʳᵉ année</span>
      </div>

    </section>
  );
}
