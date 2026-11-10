import { useEffect, useState } from 'react';
import Tuiles from './Tuiles';
import Comparaison from './Comparaison';
import TableauAnnees from './TableauAnnees';
import Graphique from './Graphique';
import GrilleSinistres from './GrilleSinistres';

const pct = (v) => v.toLocaleString('fr-FR') + ' %';

// Un curseur avec son libellé et sa valeur affichée à droite
function Curseur({ libelle, valeur, affichage, min, max, pas, onChange }) {
  return (
    <div className="champ">
      <label>
        {libelle} <output>{affichage}</output>
        <input
          type="range"
          min={min}
          max={max}
          step={pas}
          value={valeur}
          onChange={(e) => onChange(Number(e.target.value))}
        />
      </label>
    </div>
  );
}

export default function ProfilForm() {
  // Cotisations
  const [cotisationHabitation, setCotisationHabitation] = useState(0);
  const [cotisationAuto, setCotisationAuto] = useState(0);

  // Type de bonus : VARIABLE (progressif) ou FIXE
  const [mode, setMode] = useState('VARIABLE');
  const [tauxDepart, setTauxDepart] = useState(3);
  const [increment, setIncrement] = useState(0.5);
  const [plafond, setPlafond] = useState(8);
  const [tauxFixe, setTauxFixe] = useState(5);

  // Épargne
  const [rendement, setRendement] = useState(2.6);
  const [versementMensuel, setVersementMensuel] = useState(0);
  const [duree, setDuree] = useState(20);

  // Sinistres, une liste d'années par contrat
  const [sinistresAuto, setSinistresAuto] = useState([]);
  const [sinistresHabitation, setSinistresHabitation] = useState([]);

  const [resultat, setResultat] = useState(null);
  const [erreur, setErreur] = useState(null);

  const cotisation = cotisationAuto + cotisationHabitation;
  const pret = cotisation > 0;

  // On relance la simulation à chaque changement de réglage
  useEffect(() => {
    if (!pret) return;

    const controleur = new AbortController();
    const minuteur = setTimeout(async () => {
      try {
        const reponse = await fetch('/api/simulations/client', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          signal: controleur.signal,
          body: JSON.stringify({
            cotisationAuto,
            cotisationHabitation,
            versementMensuel,
            sinistresAuto: sinistresAuto.filter((a) => a <= duree),
            sinistresHabitation: sinistresHabitation.filter((a) => a <= duree),
            rendement: rendement / 100,
            duree,
            mode,
            tauxFixe: tauxFixe / 100,
            tauxDepart: tauxDepart / 100,
            increment: increment / 100,
            plafond: plafond / 100,
          }),
        });

        if (!reponse.ok) {
          const details = await reponse.json().catch(() => ({}));
          setErreur(Object.values(details).join(' · ') || `Erreur ${reponse.status}`);
          return;
        }

        setResultat(await reponse.json());
        setErreur(null);
      } catch (e) {
        if (e.name !== 'AbortError') {
          setErreur("Le serveur ne répond pas. Vérifie que le back tourne sur le port 8090.");
        }
      }
    }, 250);

    // Si un réglage change avant la fin, on annule la requête précédente
    return () => {
      clearTimeout(minuteur);
      controleur.abort();
    };
  }, [pret, cotisationAuto, cotisationHabitation, sinistresAuto, sinistresHabitation,
      rendement, versementMensuel, duree, mode, tauxFixe, tauxDepart, increment, plafond]);

  function basculerSinistre(contrat, annee) {
    const [liste, setListe] = contrat === 'auto'
      ? [sinistresAuto, setSinistresAuto]
      : [sinistresHabitation, setSinistresHabitation];

    if (liste.includes(annee)) {
      setListe(liste.filter((a) => a !== annee));
    } else {
      setListe([...liste, annee]);
    }
  }

  function aucunSinistre() {
    setSinistresAuto([]);
    setSinistresHabitation([]);
  }

  // Profil moyen : 1 sinistre auto tous les 34 ans, 1 sinistre habitation tous les 11 ans
  function profilMoyen() {
    const tousLes = (n) => Array.from({ length: Math.floor(30 / n) }, (_, i) => (i + 1) * n);
    setSinistresAuto(tousLes(34));
    setSinistresHabitation(tousLes(11));
  }

  // On enrichit le détail renvoyé par l'API pour l'affichage
  let lignes = [];
  if (pret && resultat) {
    let bonusCumul = 0;
    lignes = resultat.detailParAnnee.map((an) => {
      bonusCumul += an.bonus;
      const versementsCumul = versementMensuel * 12 * an.annee;
      return {
        ...an,
        bonusCumul,
        versementsCumul,
        interets: an.capital - bonusCumul - versementsCumul,
        tauxAuto: cotisationAuto > 0 ? an.bonusAuto / cotisationAuto : 0,
        tauxHabitation: cotisationHabitation > 0 ? an.bonusHabitation / cotisationHabitation : 0,
        sinistreAuto: sinistresAuto.includes(an.annee),
        sinistreHabitation: sinistresHabitation.includes(an.annee),
      };
    });
  }

  return (
    <div className="disposition">
      <form className="panneau" onSubmit={(e) => e.preventDefault()} autoComplete="off">
        <h2>Réglages de l'offre</h2>

        <div className="deux-colonnes">
          <div className="champ">
            <label>
              Habitation (€/an)
              <input
                type="number"
                min="0"
                step="10"
                value={cotisationHabitation}
                onChange={(e) => setCotisationHabitation(Number(e.target.value))}
              />
            </label>
          </div>
          <div className="champ">
            <label>
              Auto (€/an)
              <input
                type="number"
                min="0"
                step="10"
                value={cotisationAuto}
                onChange={(e) => setCotisationAuto(Number(e.target.value))}
              />
            </label>
          </div>
        </div>
        <p className="aide">
          Vos cotisations de cette année. Repère : 310 € en habitation et 751 € en auto en moyenne en 2026.{' '}
          <button
            type="button"
            className="btn"
            onClick={() => { setCotisationHabitation(310); setCotisationAuto(751); }}
          >
            Utiliser les moyennes
          </button>
        </p>

        <div className="champ">
          <span className="titre-champ">Type de bonus</span>
          <div className="segment" role="group" aria-label="Type de bonus">
            <button type="button" aria-pressed={mode === 'VARIABLE'} onClick={() => setMode('VARIABLE')}>
              Progressif
            </button>
            <button type="button" aria-pressed={mode === 'FIXE'} onClick={() => setMode('FIXE')}>
              Fixe
            </button>
          </div>
        </div>

        {mode === 'VARIABLE' ? (
          <div className="sous-panneau">
            <Curseur libelle="Taux de départ" valeur={tauxDepart} affichage={pct(tauxDepart)}
              min={1} max={10} pas={0.5} onChange={setTauxDepart} />
            <Curseur libelle="Hausse par année sans sinistre" valeur={increment} affichage={`+${increment.toLocaleString('fr-FR')} pt`}
              min={0} max={2} pas={0.25} onChange={setIncrement} />
            <Curseur libelle="Plafond" valeur={plafond} affichage={pct(plafond)}
              min={2} max={20} pas={0.5} onChange={setPlafond} />
            <p className="aide">Un sinistre fait perdre le bonus de l'année et remet le compteur au taux de départ.</p>
          </div>
        ) : (
          <div className="sous-panneau">
            <Curseur libelle="Taux du bonus" valeur={tauxFixe} affichage={pct(tauxFixe)}
              min={1} max={15} pas={0.5} onChange={setTauxFixe} />
          </div>
        )}

        <div>
          <Curseur libelle="Rendement annuel" valeur={rendement} affichage={pct(rendement)}
            min={0} max={5} pas={0.1} onChange={setRendement} />
          <p className="aide">2 % environ garantis, 2,6 % = moyenne des fonds euros en 2025.</p>
        </div>

        <Curseur libelle="Versements libres" valeur={versementMensuel} affichage={`${versementMensuel} €/mois`}
          min={0} max={2000} pas={5} onChange={setVersementMensuel} />

        <Curseur libelle="Durée" valeur={duree} affichage={`${duree} ans`}
          min={5} max={30} pas={1} onChange={setDuree} />
      </form>

      <main className="resultats">
        {erreur && <div className="verdict alerte">{erreur}</div>}

        {lignes.length > 0 ? (
          <>
            <Tuiles lignes={lignes} cotisation={cotisation} duree={duree} rendement={rendement}
              versementMensuel={versementMensuel} />
            <Comparaison lignes={lignes} cotisation={cotisation} duree={duree} />
            <Graphique lignes={lignes} />
          </>
        ) : (
          <section className="panneau vide">
            Saisissez vos cotisations pour voir votre épargne se constituer.
          </section>
        )}

        <GrilleSinistres
          duree={duree}
          sinistresAuto={sinistresAuto}
          sinistresHabitation={sinistresHabitation}
          onBasculer={basculerSinistre}
          onAucun={aucunSinistre}
          onProfilMoyen={profilMoyen}
        />

        {lignes.length > 0 && <TableauAnnees lignes={lignes} cotisation={cotisation} />}
      </main>
    </div>
  );
}
