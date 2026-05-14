import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
// Pages
import Login from './pages/Login';
import AdminDashboard from './pages/AdminDashboard';
import UserDashboard from './pages/UserDashboard';
import Navbar from './components/Navbar';

const App: React.FC = () => {
  const { isAuthenticated, isAdmin, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className="flex h-screen w-full items-center justify-center bg-gray-50">
        <div className="h-12 w-12 animate-spin rounded-full border-4 border-primary border-t-transparent"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen flex flex-col font-body bg-[var(--color-background)]">
      {isAuthenticated && <Navbar />}
      <main className="flex-grow flex flex-col">
        <Routes>
          <Route 
            path="/login" 
            element={isAuthenticated ? <Navigate to="/" replace /> : <Login />} 
          />
          <Route 
            path="/" 
            element={
              !isAuthenticated ? (
                <Navigate to="/login" replace />
              ) : isAdmin ? (
                <Navigate to="/admin" replace />
              ) : (
                <UserDashboard />
              )
            } 
          />
          <Route 
            path="/admin" 
            element={
              !isAuthenticated ? (
                <Navigate to="/login" replace />
              ) : !isAdmin ? (
                <Navigate to="/" replace />
              ) : (
                <AdminDashboard />
              )
            } 
          />
        </Routes>
      </main>
    </div>
  );
};

export default App;
