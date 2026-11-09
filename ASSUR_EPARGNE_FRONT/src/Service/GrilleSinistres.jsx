export default function GrilleSinistres({ duree, sinistres, onBasculer }) {
  const annees = Array.from({ length: duree }, (_, i) => i + 1); 

  return (
    <div>
      <p className="grille-titre">Sinistres auto</p>
      <p className="grille-aide">Cliquez sur une année pour déclarer ou retirer un sinistre.</p>
      <div className="grille">
        {annees.map((annee) => (
          <button
            type="button"
            key={annee}
            className={sinistres.includes(annee) ? 'annee annee--sinistre' : 'annee'}
            aria-pressed={sinistres.includes(annee)}
            onClick={() => onBasculer(annee)}
          >
            {annee}
          </button>
        ))}
      </div>
    </div>
  );
}
