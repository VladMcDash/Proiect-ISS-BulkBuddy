import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import type { AuthResponse } from '../types';
import { Role } from '../types';

interface UserInfo {
  userId: number;
  username: string;
  role: Role;
}

interface AuthContextType {
  user: UserInfo | null;
  isAuthenticated: boolean;
  isAdmin: boolean;
  login: (authData: AuthResponse) => void;
  logout: () => void;
  isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<UserInfo | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Check localStorage on initial load
    const token = localStorage.getItem('token');
    const storedUser = localStorage.getItem('user');

    if (token && storedUser) {
      try {
        setUser(JSON.parse(storedUser));
      } catch (e) {
        console.error('Failed to parse user from localStorage', e);
        localStorage.removeItem('token');
        localStorage.removeItem('user');
      }
    }
    setIsLoading(false);
  }, []);

  const login = (authData: AuthResponse) => {
    localStorage.setItem('token', authData.token);
    
    const userInfo: UserInfo = {
      userId: authData.userId,
      username: authData.username,
      role: authData.role,
    };
    
    localStorage.setItem('user', JSON.stringify(userInfo));
    setUser(userInfo);
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        isAdmin: user?.role === Role.ADMIN,
        login,
        logout,
        isLoading,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
