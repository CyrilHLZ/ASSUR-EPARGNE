import { LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid, ResponsiveContainer } from 'recharts';

const euros = new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'EUR', maximumFractionDigits: 0 });

export default function Graphique({ lignes }) {
  return (
    <div className="carte">
      <h2>Évolution de l'épargne</h2>
      <ResponsiveContainer width="100%" height={300}>
        <LineChart data={lignes}>
          <CartesianGrid strokeDasharray="3 3" vertical={false} />
          <XAxis dataKey="annee" />
          <YAxis width={80} tickFormatter={(v) => euros.format(v)} />
          <Tooltip formatter={(v) => euros.format(v)} labelFormatter={(a) => `Année ${a}`} />
          <Line className="ligne-capital" type="monotone" dataKey="capital" name="Capital" strokeWidth={2} dot={false} />
          <Line className="ligne-bonus" type="monotone" dataKey="bonus" name="Bonus" strokeWidth={2} dot={false} />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}
