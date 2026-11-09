import { useState } from 'react';
import Tuiles from './Tuiles';
import TableauAnnees from './TableauAnnees';

export default function ProfilForm() {
  const [cotisationAuto, setCotisationAuto] = useState(0);
  const [cotisationHabitation, setCotisationHabitation] = useState(0);
  const [cotisationSante, setCotisationSante] = useState(0);
  const [duree, setDuree] = useState(20);
  const [resultat, setResultat] = useState(null);

  async function simuler(e) {
    e.preventDefault();

    const reponse = await fetch('/api/simulations/client', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        cotisationAuto,
        cotisationHabitation,
        cotisationSante,
        duree,
        versementMensuel: 0,
        sinistresAuto: [], sinistresSante: [], sinistresHabitation: [],
        rendement: 0.02,
        tauxFixe: 0.10, tauxDepart: 0, increment: 0, plafond: 0,
        mode: 'FIXE',
      }),
    });

    const donnees = await reponse.json();
    setResultat(donnees);
  }

  return (
    <>
      <form onSubmit={simuler}>
        <label>
          Cotisation auto (€/an)
          <input
            type="number"
            value={cotisationAuto}
            onChange={(e) => setCotisationAuto(Number(e.target.value))}
          />
        </label>
        <label>
          Cotisation habitation (€/an)
          <input
            type="number"
            value={cotisationHabitation}
            onChange={(e) => setCotisationHabitation(Number(e.target.value))}
          />
        </label>
        <label>
          Cotisation santé (€/an)
          <input
            type="number"
            value={cotisationSante}
            onChange={(e) => setCotisationSante(Number(e.target.value))}
          />
        </label>
        <label>
          Durée (en années)
          <input
            type="range"
            min="1"
            max="40"
            value={duree}
            onChange={(e) => setDuree(Number(e.target.value))}
          />
          {duree} ans
        </label>
        <button type="submit">Simuler</button>
      </form>

      {resultat &&
        <> 
        <Tuiles capitalFinal={resultat.capitalFinal} bonusTotal={resultat.bonusTotal} />
        <TableauAnnees lignes={resultat.detailParAnnee}/>
        </>
        }
    </>
  );
}
