import './App.css'
import ProfilForm from './Service/ProfilForm'

function App() {

  return (
    <div className="page">
      <header className="entete">
        <h1>Simulateur Assur'Épargne</h1>
        <p>
          Chaque année sans sinistre responsable, une part de vos cotisations auto et habitation est versée
          sur votre épargne. Réglez l'offre à gauche et voyez ce que vous récupérez.
        </p>
      </header>
      <ProfilForm />
      <footer className="pied">
        Simulation indicative d'un concept d'offre, sans valeur contractuelle. Bonus versé en fin d'année,
        rendement appliqué au capital du début d'année. Intérêts bruts, avant prélèvements sociaux.
      </footer>
    </div>
  )
}

export default App
