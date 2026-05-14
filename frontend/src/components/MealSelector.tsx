import React, { useState, useEffect } from 'react';
import type { Meal } from '../types';
import { mealService } from '../services/meals';
import { Search, Loader2 } from 'lucide-react';

interface MealSelectorProps {
  onSelect: (mealId: number, quantity: number) => Promise<void>;
  onClose: () => void;
}

const MealSelector: React.FC<MealSelectorProps> = ({ onSelect, onClose }) => {
  const [meals, setMeals] = useState<Meal[]>([]);
  const [query, setQuery] = useState('');
  const [loading, setLoading] = useState(false);
  const [selectedMeal, setSelectedMeal] = useState<Meal | null>(null);
  const [quantity, setQuantity] = useState(100);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    const fetchMeals = async () => {
      setLoading(true);
      try {
        const data = await mealService.searchMeals(query);
        setMeals(data);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };
    
    // Debounce search
    const timer = setTimeout(() => {
      fetchMeals();
    }, 300);
    
    return () => clearTimeout(timer);
  }, [query]);

  const handleSubmit = async () => {
    if (!selectedMeal) return;
    setSubmitting(true);
    try {
      await onSelect(selectedMeal.id, quantity);
      onClose();
    } catch (error) {
      console.error(error);
      setSubmitting(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm p-4">
      <div className="bg-white rounded-2xl shadow-xl w-full max-w-lg overflow-hidden flex flex-col max-h-[85vh]">
        <div className="p-4 border-b border-zinc-100 flex justify-between items-center bg-zinc-50/50">
          <h3 className="text-lg font-display font-semibold text-zinc-900">Log a Meal</h3>
          <button onClick={onClose} className="text-zinc-400 hover:text-zinc-600 transition-colors">
            ✕
          </button>
        </div>

        {!selectedMeal ? (
          <div className="flex flex-col flex-grow overflow-hidden">
            <div className="p-4 border-b border-zinc-100">
              <div className="relative">
                <Search className="w-5 h-5 absolute left-3 top-1/2 -translate-y-1/2 text-zinc-400" />
                <input
                  type="text"
                  placeholder="Search meals..."
                  className="input-field pl-10"
                  value={query}
                  onChange={(e) => setQuery(e.target.value)}
                  autoFocus
                />
              </div>
            </div>
            <div className="overflow-y-auto p-2 flex-grow">
              {loading ? (
                <div className="flex justify-center p-8"><Loader2 className="w-6 h-6 animate-spin text-primary" /></div>
              ) : meals.length === 0 ? (
                <div className="text-center p-8 text-zinc-500">No meals found.</div>
              ) : (
                <div className="space-y-1">
                  {meals.map(meal => (
                    <button
                      key={meal.id}
                      onClick={() => setSelectedMeal(meal)}
                      className="w-full flex items-center p-3 rounded-xl hover:bg-zinc-50 transition-colors text-left"
                    >
                      {meal.imageUrl ? (
                        <img src={meal.imageUrl} alt="" className="w-12 h-12 rounded-lg object-cover bg-zinc-100 mr-4" />
                      ) : (
                        <div className="w-12 h-12 rounded-lg bg-zinc-100 mr-4" />
                      )}
                      <div>
                        <div className="font-semibold text-zinc-900">{meal.name}</div>
                        <div className="text-xs text-zinc-500">{meal.calories} kcal • {meal.protein}g protein <span className="opacity-75">/ 100g</span></div>
                      </div>
                    </button>
                  ))}
                </div>
              )}
            </div>
          </div>
        ) : (
          <div className="p-6 flex flex-col items-center">
            {selectedMeal.imageUrl ? (
              <img src={selectedMeal.imageUrl} alt="" className="w-32 h-32 rounded-2xl object-cover shadow-md mb-4" />
            ) : (
              <div className="w-32 h-32 rounded-2xl bg-zinc-100 mb-4" />
            )}
            <h3 className="text-xl font-display font-bold text-zinc-900">{selectedMeal.name}</h3>
            <p className="text-sm text-zinc-500 mb-6">{selectedMeal.calories} kcal • {selectedMeal.protein}g protein per 100g</p>
            
            <div className="w-full space-y-4 bg-zinc-50 p-4 rounded-xl border border-zinc-100">
              <label className="label text-center">Quantity (grams)</label>
              <div className="flex items-center justify-center space-x-4">
                <button 
                  onClick={() => setQuantity(Math.max(1, quantity - 10))}
                  className="w-10 h-10 rounded-full bg-white border border-zinc-200 text-zinc-600 font-bold hover:bg-zinc-50 hover:border-zinc-300 transition-colors shadow-sm"
                >
                  -
                </button>
                <input 
                  type="number" 
                  min="0.1"
                  step="0.1"
                  value={quantity}
                  onChange={(e) => setQuantity(Math.max(0.1, parseFloat(e.target.value) || 0))}
                  className="w-24 text-center input-field font-bold text-lg"
                />
                <button 
                  onClick={() => setQuantity(quantity + 10)}
                  className="w-10 h-10 rounded-full bg-white border border-zinc-200 text-zinc-600 font-bold hover:bg-zinc-50 hover:border-zinc-300 transition-colors shadow-sm"
                >
                  +
                </button>
              </div>
              
              <div className="flex justify-between items-center pt-4 mt-2 border-t border-zinc-200">
                <div className="text-sm text-zinc-600">Total:</div>
                <div className="text-right">
                  <span className="font-bold text-primary">{Math.round((selectedMeal.calories * quantity) / 100)} kcal</span>
                  <span className="text-zinc-400 mx-2">•</span>
                  <span className="font-bold text-secondary">{Math.round((selectedMeal.protein * quantity) / 100)}g pro</span>
                </div>
              </div>
            </div>
            
            <div className="flex w-full space-x-3 mt-6">
              <button onClick={() => setSelectedMeal(null)} className="btn-secondary flex-1">Back</button>
              <button onClick={handleSubmit} disabled={submitting} className="btn-primary flex-1 flex justify-center items-center">
                {submitting ? <Loader2 className="w-5 h-5 animate-spin" /> : 'Log Meal'}
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default MealSelector;
