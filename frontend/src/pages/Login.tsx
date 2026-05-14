import React, { useEffect, useState } from 'react';
import { authService } from '../services/auth';
import { useAuth } from '../context/AuthContext';
import type { User } from '../types';
import { Dumbbell, ShieldCheck, User as UserIcon, Loader2 } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const Login: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [loggingInId, setLoggingInId] = useState<number | null>(null);
  
  const { login } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const data = await authService.getAvailableUsers();
        setUsers(data);
      } catch (err) {
        setError('Failed to load available users. Is the backend running?');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchUsers();
  }, []);

  const handleSelectUser = async (userId: number) => {
    try {
      setLoggingInId(userId);
      setError('');
      const authData = await authService.selectUser(userId);
      login(authData);
      navigate('/');
    } catch (err: any) {
      setError(err.response?.data?.error || 'Login failed');
      setLoggingInId(null);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-zinc-50 px-4 sm:px-6 lg:px-8 relative overflow-hidden">
      {/* Decorative Background Elements */}
      <div className="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] bg-primary/20 rounded-full blur-3xl" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] bg-secondary/15 rounded-full blur-3xl" />

      <div className="max-w-md w-full space-y-8 bg-white/80 backdrop-blur-xl p-10 rounded-2xl shadow-xl border border-white/20 relative z-10">
        <div className="text-center">
          <div className="mx-auto h-16 w-16 bg-primary/10 rounded-2xl flex items-center justify-center mb-4 ring-1 ring-primary/20">
            <Dumbbell className="h-8 w-8 text-primary" />
          </div>
          <h2 className="mt-2 text-center text-3xl font-display font-extrabold text-zinc-900 tracking-tight">
            Welcome to BulkBuddy
          </h2>
          <p className="mt-2 text-center text-sm text-zinc-500">
            For development, please select an account below. No password required.
          </p>
        </div>

        {error && (
          <div className="bg-red-50 text-red-600 p-3 rounded-lg text-sm font-medium border border-red-100 flex items-center justify-center">
            {error}
          </div>
        )}

        <div className="mt-8 space-y-4">
          {loading ? (
            <div className="flex justify-center items-center py-8">
              <Loader2 className="w-8 h-8 animate-spin text-primary" />
            </div>
          ) : users.length === 0 ? (
            <div className="text-center text-zinc-500 py-4 bg-zinc-50 rounded-lg border border-zinc-200">
              No users found. Please restart the backend.
            </div>
          ) : (
            users.map((user) => (
              <button
                key={user.id}
                onClick={() => handleSelectUser(user.id)}
                disabled={loggingInId !== null}
                className={`w-full flex items-center justify-between p-4 rounded-xl border-2 transition-all duration-200 ${
                  loggingInId === user.id
                    ? 'border-primary bg-blue-50/50'
                    : 'border-zinc-100 hover:border-primary/30 hover:bg-zinc-50 bg-white'
                }`}
              >
                <div className="flex items-center space-x-4">
                  <div className={`p-2 rounded-full ${user.role === 'ADMIN' ? 'bg-secondary/10 text-secondary' : 'bg-zinc-100 text-zinc-500'}`}>
                    {user.role === 'ADMIN' ? <ShieldCheck className="w-5 h-5" /> : <UserIcon className="w-5 h-5" />}
                  </div>
                  <div className="text-left">
                    <p className="text-sm font-bold text-zinc-900">{user.username}</p>
                    <p className="text-xs font-medium text-zinc-500 tracking-wide uppercase mt-0.5">{user.role}</p>
                  </div>
                </div>
                {loggingInId === user.id ? (
                  <Loader2 className="w-5 h-5 animate-spin text-primary" />
                ) : (
                  <div className="text-primary opacity-0 group-hover:opacity-100 transition-opacity">→</div>
                )}
              </button>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default Login;
