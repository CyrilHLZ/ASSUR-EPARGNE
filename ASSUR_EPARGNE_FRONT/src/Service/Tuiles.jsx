const euros = new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'EUR' });

export default function Tuiles({ capitalFinal , bonusTotal }) {  
  return (
    <div className="tuiles">
      <div className="carte tuile tuile--capital">
        <span>Capital final</span>
        <strong>{euros.format(capitalFinal)}</strong>
      </div>

      <div className="carte tuile tuile--bonus">
        <span>Bonus total</span>
        <strong>{euros.format(bonusTotal)}</strong>
      </div>
    </div>
  );
}
