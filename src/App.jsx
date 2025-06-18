
import { Routes, Route } from 'react-router-dom';
import { BrowserRouter } from "react-router-dom";
import Login from './Pages/Account/Login';
import Register from './Pages/Account/Register';
function App() {

  return (
    <>
      <BrowserRouter>


        <Routes>
          <Route exact path="/" element={<Login />} />
           <Route exact path="/register" element={<Register />} />

        </Routes>

      </BrowserRouter>
    </>

  )
}

export default App
