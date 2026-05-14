import React, { useEffect, useState } from 'react';
import type { Meal, Workout } from '../types';
import { mealService } from '../services/meals';
import { workoutService } from '../services/workouts';
import { Plus, Trash2, Edit2, Utensils, Activity } from 'lucide-react';

const AdminDashboard: React.FC = () => {
  const [meals, setMeals] = useState<Meal[]>([]);
  const [workouts, setWorkouts] = useState<Workout[]>([]);
  const [loading, setLoading] = useState(true);

  // Simple state for meal form
  const [showMealForm, setShowMealForm] = useState(false);
  const [mealForm, setMealForm] = useState({ name: '', calories: 0, protein: 0, imageUrl: '' });

  // Simple state for workout form
  const [showWorkoutForm, setShowWorkoutForm] = useState(false);
  const [workoutForm, setWorkoutForm] = useState({ type: '', caloriesBurned: 0 });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    setLoading(true);
    try {
      const [mealsData, workoutsData] = await Promise.all([
        mealService.getAllMeals(),
        workoutService.getAllWorkouts(),
      ]);
      setMeals(mealsData);
      setWorkouts(workoutsData);
    } catch (error) {
      console.error('Failed to fetch data', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateMeal = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await mealService.createMeal(mealForm);
      setShowMealForm(false);
      setMealForm({ name: '', calories: 0, protein: 0, imageUrl: '' });
      fetchData();
    } catch (error) {
      console.error(error);
    }
  };

  const handleDeleteMeal = async (id: number) => {
    try {
      await mealService.deleteMeal(id);
      fetchData();
    } catch (error) {
      console.error(error);
    }
  };

  const handleCreateWorkout = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await workoutService.createWorkout(workoutForm);
      setShowWorkoutForm(false);
      setWorkoutForm({ type: '', caloriesBurned: 0 });
      fetchData();
    } catch (error) {
      console.error(error);
    }
  };

  const handleDeleteWorkout = async (id: number) => {
    try {
      await workoutService.deleteWorkout(id);
      fetchData();
    } catch (error) {
      console.error(error);
    }
  };

  if (loading) {
    return <div className="p-8 text-center text-zinc-500">Loading admin dashboard...</div>;
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 w-full">
      <div className="mb-8">
        <h1 className="text-3xl font-display font-bold text-zinc-900">Admin Dashboard</h1>
        <p className="text-zinc-500 mt-2">Manage standardized meals and workouts for all users.</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        
        {/* Meals Section */}
        <section className="bg-white rounded-2xl shadow-sm border border-zinc-200 overflow-hidden">
          <div className="p-6 border-b border-zinc-200 bg-zinc-50/50 flex justify-between items-center">
            <div className="flex items-center space-x-3">
              <div className="p-2 bg-orange-100 text-orange-600 rounded-lg">
                <Utensils className="w-5 h-5" />
              </div>
              <h2 className="text-xl font-display font-semibold text-zinc-900">Standard Meals</h2>
            </div>
            <button
              onClick={() => setShowMealForm(!showMealForm)}
              className="btn-primary flex items-center space-x-2 py-2 text-sm"
            >
              <Plus className="w-4 h-4" />
              <span>Add Meal</span>
            </button>
          </div>

          {showMealForm && (
            <div className="p-6 bg-blue-50/30 border-b border-zinc-200">
              <form onSubmit={handleCreateMeal} className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label className="label">Name</label>
                    <input type="text" required className="input-field" value={mealForm.name} onChange={e => setMealForm({...mealForm, name: e.target.value})} />
                  </div>
                  <div>
                    <label className="label">Image URL</label>
                    <input type="url" className="input-field" value={mealForm.imageUrl} onChange={e => setMealForm({...mealForm, imageUrl: e.target.value})} />
                  </div>
                  <div>
                    <label className="label">Calories (per 100g)</label>
                    <input type="number" min="0" required className="input-field" value={mealForm.calories || ''} onChange={e => setMealForm({...mealForm, calories: parseInt(e.target.value)})} />
                  </div>
                  <div>
                    <label className="label">Protein (per 100g)</label>
                    <input type="number" min="0" required className="input-field" value={mealForm.protein || ''} onChange={e => setMealForm({...mealForm, protein: parseInt(e.target.value)})} />
                  </div>
                </div>
                <div className="flex justify-end space-x-3 pt-2">
                  <button type="button" onClick={() => setShowMealForm(false)} className="btn-ghost">Cancel</button>
                  <button type="submit" className="btn-primary">Save Meal</button>
                </div>
              </form>
            </div>
          )}

          <div className="divide-y divide-zinc-200 max-h-[600px] overflow-y-auto">
            {meals.map(meal => (
              <div key={meal.id} className="p-4 sm:px-6 hover:bg-zinc-50 flex items-center justify-between transition-colors">
                <div className="flex items-center space-x-4">
                  {meal.imageUrl ? (
                    <img src={meal.imageUrl} alt={meal.name} className="w-12 h-12 rounded-lg object-cover bg-zinc-100" />
                  ) : (
                    <div className="w-12 h-12 rounded-lg bg-zinc-100 flex items-center justify-center">
                      <Utensils className="w-5 h-5 text-zinc-400" />
                    </div>
                  )}
                  <div>
                    <h3 className="text-sm font-semibold text-zinc-900">{meal.name}</h3>
                    <p className="text-xs text-zinc-500 mt-0.5">{meal.calories} kcal • {meal.protein}g protein <span className="text-zinc-400">(per 100g)</span></p>
                  </div>
                </div>
                <button onClick={() => handleDeleteMeal(meal.id)} className="p-2 text-zinc-400 hover:text-error transition-colors rounded-lg hover:bg-red-50">
                  <Trash2 className="w-4 h-4" />
                </button>
              </div>
            ))}
            {meals.length === 0 && <div className="p-8 text-center text-zinc-500 text-sm">No meals found.</div>}
          </div>
        </section>

        {/* Workouts Section */}
        <section className="bg-white rounded-2xl shadow-sm border border-zinc-200 overflow-hidden">
          <div className="p-6 border-b border-zinc-200 bg-zinc-50/50 flex justify-between items-center">
            <div className="flex items-center space-x-3">
              <div className="p-2 bg-blue-100 text-blue-600 rounded-lg">
                <Activity className="w-5 h-5" />
              </div>
              <h2 className="text-xl font-display font-semibold text-zinc-900">Standard Workouts</h2>
            </div>
            <button
              onClick={() => setShowWorkoutForm(!showWorkoutForm)}
              className="btn-primary flex items-center space-x-2 py-2 text-sm"
            >
              <Plus className="w-4 h-4" />
              <span>Add Workout</span>
            </button>
          </div>

          {showWorkoutForm && (
            <div className="p-6 bg-blue-50/30 border-b border-zinc-200">
              <form onSubmit={handleCreateWorkout} className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label className="label">Workout Type</label>
                    <input type="text" required className="input-field" value={workoutForm.type} onChange={e => setWorkoutForm({...workoutForm, type: e.target.value})} />
                  </div>
                  <div>
                    <label className="label">Calories Burned (per hour)</label>
                    <input type="number" min="0" required className="input-field" value={workoutForm.caloriesBurned || ''} onChange={e => setWorkoutForm({...workoutForm, caloriesBurned: parseInt(e.target.value)})} />
                  </div>
                </div>
                <div className="flex justify-end space-x-3 pt-2">
                  <button type="button" onClick={() => setShowWorkoutForm(false)} className="btn-ghost">Cancel</button>
                  <button type="submit" className="btn-primary">Save Workout</button>
                </div>
              </form>
            </div>
          )}

          <div className="divide-y divide-zinc-200 max-h-[600px] overflow-y-auto">
            {workouts.map(workout => (
              <div key={workout.id} className="p-4 sm:px-6 hover:bg-zinc-50 flex items-center justify-between transition-colors">
                <div className="flex items-center space-x-4">
                  <div className="w-10 h-10 rounded-full bg-zinc-100 flex items-center justify-center">
                    <Activity className="w-4 h-4 text-zinc-500" />
                  </div>
                  <div>
                    <h3 className="text-sm font-semibold text-zinc-900">{workout.type}</h3>
                    <p className="text-xs text-zinc-500 mt-0.5">{workout.caloriesBurned} kcal <span className="text-zinc-400">burned per hour</span></p>
                  </div>
                </div>
                <button onClick={() => handleDeleteWorkout(workout.id)} className="p-2 text-zinc-400 hover:text-error transition-colors rounded-lg hover:bg-red-50">
                  <Trash2 className="w-4 h-4" />
                </button>
              </div>
            ))}
            {workouts.length === 0 && <div className="p-8 text-center text-zinc-500 text-sm">No workouts found.</div>}
          </div>
        </section>

      </div>
    </div>
  );
};

export default AdminDashboard;
