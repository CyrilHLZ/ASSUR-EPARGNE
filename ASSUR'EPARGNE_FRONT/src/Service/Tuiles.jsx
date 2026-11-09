const euros = new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'EUR' });

export default function Tuiles({ capitalFinal , bonusTotal }) {  
  return (
    <div>
      <div>
        <span>Capital final</span>
        <strong>{euros.format(capitalFinal)}</strong>
      </div>

      <div>
        <span>Bonus total</span>
        <strong>{euros.format(bonusTotal)}</strong>
      </div>
    </div>
  );
}
