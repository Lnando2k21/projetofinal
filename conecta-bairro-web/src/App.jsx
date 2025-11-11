import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import PrivateLayout from './components/PrivateLayout';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';
import ServicosPage from './pages/ServicosPage';
import Navbar from './components/layout/Navbar';
import Footer from './components/layout/Footer';

const PublicLayout = ({ children }) => (
  <>
    <Navbar />
    <main className="min-h-screen bg-gray-50">{children}</main>
    <Footer />
  </>
);

export default function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          {/* Rotas Públicas */}
          <Route
            path="/"
            element={
              <PublicLayout>
                <HomePage />
              </PublicLayout>
            }
          />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/registro" element={<RegisterPage />} />
          <Route
            path="/servicos"
            element={
              <PublicLayout>
                <ServicosPage />
              </PublicLayout>
            }
          />
          
          {/* Rotas protegidas */}
          <Route element={<PrivateLayout />}>
            <Route path="/dashboard" element={<DashboardPage />} />
            <Route path="/meus-servicos" element={<ServicosPage />} />
          </Route>
        </Routes>
      </Router>
    </AuthProvider>
  );
}
