import { useState } from 'react';

export default function ProfilForm() {
  const [cotisationAuto, setCotisationAuto] = useState(0);
  const [cotisationHabitation, setCotisationHabitation] = useState(0);
  const [cotisationSante, setCotisationSante] = useState(0);

  return (
    <form>
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
    </form>
  );
}
