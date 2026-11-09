export default function GrilleSinistres({ duree, sinistres, onBasculer }) {
  const annees = Array.from({ length: duree }, (_, i) => i + 1); 

  return (
    <div>
      {annees.map((annee) => (
        <button
          type="button"
          key={annee}
          onClick={() => onBasculer(annee)}
        >
          {annee} {sinistres.includes(annee) ? '💥' : ''}
        </button>
      ))}
    </div>
  );
}
