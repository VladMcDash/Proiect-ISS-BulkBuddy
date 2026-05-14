import React, { useEffect, useState } from 'react';
import type { DailyLog, Notification } from '../types';
import { dashboardService } from '../services/dashboard';
import { notificationService } from '../services/notifications';
import MacroProgressBar from '../components/MacroProgressBar';
import NotificationAlert from '../components/NotificationAlert';
import MealSelector from '../components/MealSelector';
import WorkoutSelector from '../components/WorkoutSelector';
import { Plus, Trash2, Utensils, Activity, Target, Flame, ChevronRight } from 'lucide-react';
import { clsx } from 'clsx';

const UserDashboard: React.FC = () => {
  const [log, setLog] = useState<DailyLog | null>(null);
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [loading, setLoading] = useState(true);
  
  const [showMealModal, setShowMealModal] = useState(false);
  const [showWorkoutModal, setShowWorkoutModal] = useState(false);
  const [showGoalModal, setShowGoalModal] = useState(false);
  const [goalForm, setGoalForm] = useState({ dailyCalorieGoal: 0, dailyProteinGoal: 0 });

  const fetchData = async () => {
    try {
      const [logData, notifData] = await Promise.all([
        dashboardService.getTodaySummary(),
        notificationService.getUnread()
      ]);
      setLog(logData);
      setNotifications(notifData);
      setGoalForm({
        dailyCalorieGoal: logData.calorieGoal,
        dailyProteinGoal: logData.proteinGoal
      });
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleMarkAsRead = async (id: number) => {
    try {
      await notificationService.markAsRead(id);
      setNotifications(prev => prev.filter(n => n.id !== id));
    } catch (error) {
      console.error(error);
    }
  };

  const handleAddMeal = async (mealId: number, quantity: number) => {
    await dashboardService.addConsumedMeal(mealId, quantity);
    fetchData();
  };

  const handleAddWorkout = async (workoutId: number, duration: number) => {
    await dashboardService.addPerformedWorkout(workoutId, duration);
    fetchData();
  };

  const handleRemoveMeal = async (id: number) => {
    await dashboardService.removeConsumedMeal(id);
    fetchData();
  };

  const handleRemoveWorkout = async (id: number) => {
    await dashboardService.removePerformedWorkout(id);
    fetchData();
  };

  const handleUpdateGoals = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await dashboardService.updateGoals(goalForm);
      setShowGoalModal(false);
      fetchData();
    } catch (error) {
      console.error(error);
    }
  };

  if (loading || !log) {
    return <div className="p-8 text-center text-zinc-500">Loading your daily summary...</div>;
  }

  // Calculate Net Calories
  const netCalories = log.caloriesConsumed - log.caloriesBurned;

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 w-full flex flex-col lg:flex-row gap-8">
      
      {/* LEFT COLUMN: Summary & Macros */}
      <div className="w-full lg:w-1/3 flex flex-col space-y-6">
        
        {/* Notifications */}
        {notifications.length > 0 && (
          <div className="space-y-3">
            {notifications.map(notif => (
              <NotificationAlert key={notif.id} notification={notif} onMarkAsRead={handleMarkAsRead} />
            ))}
          </div>
        )}

        {/* Today's Summary Card */}
        <div className="card bg-white shadow-sm border border-zinc-200">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-xl font-display font-bold text-zinc-900">Today's Macros</h2>
            <button 
              onClick={() => setShowGoalModal(true)}
              className="text-primary text-sm font-medium hover:underline flex items-center"
            >
              <Target className="w-4 h-4 mr-1" />
              Edit Goals
            </button>
          </div>

          <div className="space-y-8">
            {/* Calories Progress */}
            <MacroProgressBar 
              label="Net Calories" 
              subLabel="(Consumed - Burned)"
              consumed={netCalories} 
              goal={log.calorieGoal} 
              unit="kcal" 
              colorClass="bg-primary" 
            />

            {/* Protein Progress */}
            <MacroProgressBar 
              label="Protein" 
              consumed={log.proteinConsumed} 
              goal={log.proteinGoal} 
              unit="g" 
              colorClass="bg-secondary" 
            />
          </div>

          {/* Quick Stats Grid */}
          <div className="grid grid-cols-2 gap-4 mt-8 pt-6 border-t border-zinc-100">
            <div className="bg-zinc-50 p-4 rounded-xl text-center">
              <div className="flex justify-center mb-2 text-zinc-400">
                <Utensils className="w-5 h-5" />
              </div>
              <p className="text-xs font-semibold text-zinc-500 uppercase tracking-wider mb-1">Eaten</p>
              <p className="text-xl font-bold text-zinc-900">{log.caloriesConsumed}</p>
            </div>
            <div className="bg-orange-50 p-4 rounded-xl text-center">
              <div className="flex justify-center mb-2 text-orange-400">
                <Flame className="w-5 h-5" />
              </div>
              <p className="text-xs font-semibold text-orange-600/70 uppercase tracking-wider mb-1">Burned</p>
              <p className="text-xl font-bold text-orange-600">{log.caloriesBurned}</p>
            </div>
          </div>
        </div>
      </div>

      {/* RIGHT COLUMN: Daily Log Lists */}
      <div className="w-full lg:w-2/3 flex flex-col space-y-6">
        
        {/* Meals Log */}
        <section className="bg-white rounded-2xl shadow-sm border border-zinc-200 overflow-hidden">
          <div className="p-5 sm:p-6 border-b border-zinc-200 bg-zinc-50/50 flex justify-between items-center">
            <h2 className="text-xl font-display font-semibold text-zinc-900">Meals Log</h2>
            <button 
              onClick={() => setShowMealModal(true)}
              className="btn-primary flex items-center space-x-2 py-2 text-sm"
            >
              <Plus className="w-4 h-4" />
              <span>Add Meal</span>
            </button>
          </div>
          <div className="divide-y divide-zinc-100 max-h-[400px] overflow-y-auto">
            {log.consumedMeals?.map(cm => (
              <div key={cm.id} className="p-4 sm:p-6 flex items-center justify-between hover:bg-zinc-50 transition-colors group">
                <div className="flex items-center space-x-4">
                  {cm.mealImageUrl ? (
                    <img src={cm.mealImageUrl} alt="" className="w-12 h-12 rounded-xl object-cover bg-zinc-100" />
                  ) : (
                    <div className="w-12 h-12 rounded-xl bg-zinc-100 flex items-center justify-center">
                      <Utensils className="w-5 h-5 text-zinc-400" />
                    </div>
                  )}
                  <div>
                    <h3 className="font-semibold text-zinc-900">{cm.mealName}</h3>
                    <p className="text-xs text-zinc-500 mt-0.5">
                      {cm.quantity}g
                    </p>
                  </div>
                </div>
                <div className="flex items-center space-x-6">
                  <div className="text-right">
                    <p className="font-bold text-primary">{cm.totalCalories} kcal</p>
                    <p className="text-xs font-medium text-secondary">{cm.totalProtein}g protein</p>
                  </div>
                  <button 
                    onClick={() => handleRemoveMeal(cm.id)}
                    className="p-2 text-zinc-300 hover:text-error transition-colors rounded-lg hover:bg-red-50 lg:opacity-0 lg:group-hover:opacity-100 focus:opacity-100"
                  >
                    <Trash2 className="w-5 h-5" />
                  </button>
                </div>
              </div>
            ))}
            {(!log.consumedMeals || log.consumedMeals.length === 0) && (
              <div className="p-8 text-center flex flex-col items-center justify-center">
                <Utensils className="w-8 h-8 text-zinc-300 mb-3" />
                <p className="text-zinc-500 font-medium">No meals logged yet.</p>
                <p className="text-zinc-400 text-sm mt-1">Track what you eat to hit your goals.</p>
              </div>
            )}
          </div>
        </section>

        {/* Workouts Log */}
        <section className="bg-white rounded-2xl shadow-sm border border-zinc-200 overflow-hidden">
          <div className="p-5 sm:p-6 border-b border-zinc-200 bg-zinc-50/50 flex justify-between items-center">
            <h2 className="text-xl font-display font-semibold text-zinc-900">Workouts Log</h2>
            <button 
              onClick={() => setShowWorkoutModal(true)}
              className="btn-secondary flex items-center space-x-2 py-2 text-sm"
            >
              <Plus className="w-4 h-4" />
              <span>Add Workout</span>
            </button>
          </div>
          <div className="divide-y divide-zinc-100 max-h-[400px] overflow-y-auto">
            {log.performedWorkouts?.map(pw => (
              <div key={pw.id} className="p-4 sm:p-6 flex items-center justify-between hover:bg-zinc-50 transition-colors group">
                <div className="flex items-center space-x-4">
                  <div className="w-12 h-12 rounded-xl bg-blue-50 flex flex-shrink-0 items-center justify-center text-blue-600">
                    <Activity className="w-6 h-6" />
                  </div>
                  <div>
                    <h3 className="font-semibold text-zinc-900">{pw.workoutType}</h3>
                    <p className="text-xs text-zinc-500 mt-0.5">{pw.durationMinutes} minutes</p>
                  </div>
                </div>
                <div className="flex items-center space-x-6">
                  <div className="text-right">
                    <p className="font-bold text-orange-600">-{pw.totalCaloriesBurned} kcal</p>
                  </div>
                  <button 
                    onClick={() => handleRemoveWorkout(pw.id)}
                    className="p-2 text-zinc-300 hover:text-error transition-colors rounded-lg hover:bg-red-50 lg:opacity-0 lg:group-hover:opacity-100 focus:opacity-100"
                  >
                    <Trash2 className="w-5 h-5" />
                  </button>
                </div>
              </div>
            ))}
            {(!log.performedWorkouts || log.performedWorkouts.length === 0) && (
              <div className="p-8 text-center flex flex-col items-center justify-center">
                <Activity className="w-8 h-8 text-zinc-300 mb-3" />
                <p className="text-zinc-500 font-medium">No workouts logged yet.</p>
                <p className="text-zinc-400 text-sm mt-1">Get moving and earn extra calories!</p>
              </div>
            )}
          </div>
        </section>

      </div>

      {/* Modals */}
      {showMealModal && <MealSelector onClose={() => setShowMealModal(false)} onSelect={handleAddMeal} />}
      {showWorkoutModal && <WorkoutSelector onClose={() => setShowWorkoutModal(false)} onSelect={handleAddWorkout} />}
      
      {showGoalModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm p-4">
          <div className="bg-white rounded-2xl shadow-xl w-full max-w-sm overflow-hidden p-6">
            <h3 className="text-lg font-display font-semibold text-zinc-900 mb-4">Edit Daily Goals</h3>
            <form onSubmit={handleUpdateGoals} className="space-y-4">
              <div>
                <label className="label">Calorie Goal (kcal)</label>
                <input 
                  type="number" min="0" required 
                  className="input-field" 
                  value={goalForm.dailyCalorieGoal} 
                  onChange={e => setGoalForm({...goalForm, dailyCalorieGoal: parseInt(e.target.value) || 0})} 
                />
              </div>
              <div>
                <label className="label">Protein Goal (g)</label>
                <input 
                  type="number" min="0" required 
                  className="input-field" 
                  value={goalForm.dailyProteinGoal} 
                  onChange={e => setGoalForm({...goalForm, dailyProteinGoal: parseInt(e.target.value) || 0})} 
                />
              </div>
              <div className="flex space-x-3 pt-4">
                <button type="button" onClick={() => setShowGoalModal(false)} className="btn-secondary flex-1">Cancel</button>
                <button type="submit" className="btn-primary flex-1">Save</button>
              </div>
            </form>
          </div>
        </div>
      )}

    </div>
  );
};

export default UserDashboard;
