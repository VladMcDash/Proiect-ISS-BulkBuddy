import React from 'react';
import { useAuth } from '../context/AuthContext';
import { LogOut, Dumbbell, User as UserIcon } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const Navbar: React.FC = () => {
  const { user, logout, isAdmin } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="sticky top-0 z-50 w-full bg-white border-b border-zinc-200 shadow-sm">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          {/* Logo Section */}
          <div className="flex items-center space-x-2">
            <div className="p-2 bg-primary/10 rounded-lg">
              <Dumbbell className="w-6 h-6 text-primary" />
            </div>
            <span className="font-display font-bold text-xl tracking-tight text-zinc-900">
              Bulk<span className="text-primary">Buddy</span>
            </span>
          </div>

          {/* User Section */}
          <div className="flex items-center space-x-6">
            <div className="flex items-center space-x-2 bg-zinc-50 py-1.5 px-3 rounded-full border border-zinc-200">
              <UserIcon className="w-4 h-4 text-zinc-500" />
              <span className="text-sm font-medium text-zinc-700">
                {user?.username} <span className="text-zinc-400 font-normal ml-1">({isAdmin ? 'Admin' : 'User'})</span>
              </span>
            </div>
            <button
              onClick={handleLogout}
              className="flex items-center space-x-1 text-sm font-medium text-zinc-500 hover:text-error transition-colors"
            >
              <span>Logout</span>
              <LogOut className="w-4 h-4" />
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
