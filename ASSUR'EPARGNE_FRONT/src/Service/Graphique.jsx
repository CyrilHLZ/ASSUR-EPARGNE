import { LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid, ResponsiveContainer } from 'recharts';

export default function Graphique({ lignes }) {
  return (
    <ResponsiveContainer width="100%" height={300}>
      <LineChart data={lignes}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="annee" />
        <YAxis />
        <Tooltip />
        <Line type="monotone" dataKey="capital" name="Capital" />
        <Line type="monotone" dataKey="bonus" name="Bonus" stroke="#82ca9d" />
      </LineChart>
    </ResponsiveContainer>
  );
}
