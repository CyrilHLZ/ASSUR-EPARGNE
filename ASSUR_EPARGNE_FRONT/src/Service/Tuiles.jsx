const euros = (v) => Math.round(v).toLocaleString('fr-FR') + ' €';

export default function Tuiles({ lignes, cotisation, duree, rendement, versementMensuel }) {
  const derniere = lignes[lignes.length - 1];

  return (
    <section className="tuiles" aria-label="Résultats">
      <div className="tuile principale">
        <span className="k">Capital au terme</span>
        <span className="v">{euros(derniere.capital)}</span>
        <span className="s">après {duree} ans</span>
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
        <span className="s">avec un rendement de {rendement.toLocaleString('fr-FR')} %</span>
      </div>

      <div className="tuile">
        <span className="k"><span className="pastille" style={{ background: 'var(--ligne)' }} />Cotisations payées</span>
        <span className="v">{euros(cotisation * duree)}</span>
        <span className="s">{euros(cotisation)} par an</span>
      </div>

    </section>
  );
}
