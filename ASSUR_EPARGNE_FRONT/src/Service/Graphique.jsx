import { AreaChart, Area, XAxis, YAxis, Tooltip, CartesianGrid, ReferenceLine, ResponsiveContainer } from 'recharts';

const euros = (v) => Math.round(v).toLocaleString('fr-FR') + ' €';

export default function Graphique({ lignes }) {
  // Point de départ à 0 pour que la courbe parte de l'origine
  const donnees = [{ annee: 0, bonusCumul: 0, versementsCumul: 0, interets: 0 }, ...lignes];
  const anneesSinistre = lignes.filter((l) => l.sinistreAuto || l.sinistreHabitation).map((l) => l.annee);

  const titre = anneesSinistre.length
    ? `Évolution de votre épargne (${anneesSinistre.length} année${anneesSinistre.length > 1 ? 's' : ''} avec sinistre)`
    : 'Évolution de votre épargne, sans aucun sinistre';

  return (
    <section className="panneau">
      <h2>{titre}</h2>
      <ResponsiveContainer width="100%" height={300}>
        <AreaChart data={donnees} margin={{ top: 10, right: 16, left: 0, bottom: 0 }}>
          <CartesianGrid stroke="var(--ligne)" vertical={false} />
          <XAxis dataKey="annee" tickFormatter={(a) => `${a} an${a > 1 ? 's' : ''}`}
            stroke="var(--ligne)" tick={{ fill: 'var(--muted)', fontSize: 12 }} />
          <YAxis width={80} tickFormatter={euros}
            stroke="var(--ligne)" tick={{ fill: 'var(--muted)', fontSize: 12 }} />
          <Tooltip
            formatter={(v) => euros(v)}
            labelFormatter={(a) => `Année ${a}`}
            contentStyle={{ background: 'var(--surface)', border: '1px solid var(--ligne)', borderRadius: 8 }}
          />
          {anneesSinistre.map((a) => (
            <ReferenceLine key={a} x={a} stroke="var(--danger)" strokeDasharray="3 3" />
          ))}
          {/* Ordre d'empilement, de bas en haut : bonus, versements, intérêts */}
          <Area type="monotone" dataKey="bonusCumul" name="Bonus prudence" stackId="1"
            stroke="var(--accent)" fill="var(--accent)" fillOpacity={0.85} />
          <Area type="monotone" dataKey="versementsCumul" name="Versements libres" stackId="1"
            stroke="var(--second)" fill="var(--second)" fillOpacity={0.7} />
          <Area type="monotone" dataKey="interets" name="Intérêts" stackId="1"
            stroke="var(--troisieme)" fill="var(--troisieme)" fillOpacity={0.85} />
        </AreaChart>
      </ResponsiveContainer>
      <div className="legende">
        <span><span className="pastille" style={{ background: 'var(--accent)' }} />Bonus prudence</span>
        <span><span className="pastille" style={{ background: 'var(--second)' }} />Versements libres</span>
        <span><span className="pastille" style={{ background: 'var(--troisieme)' }} />Intérêts</span>
        <span><span className="pastille" style={{ background: 'var(--danger)' }} />Année avec sinistre</span>
      </div>
    </section>
  );
}
