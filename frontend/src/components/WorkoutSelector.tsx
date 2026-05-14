import React, { useState, useEffect } from 'react';
import type { Workout } from '../types';
import { workoutService } from '../services/workouts';
import { Search, Loader2, Activity } from 'lucide-react';

interface WorkoutSelectorProps {
  onSelect: (workoutId: number, durationMinutes: number) => Promise<void>;
  onClose: () => void;
}

const WorkoutSelector: React.FC<WorkoutSelectorProps> = ({ onSelect, onClose }) => {
  const [workouts, setWorkouts] = useState<Workout[]>([]);
  const [query, setQuery] = useState('');
  const [loading, setLoading] = useState(false);
  const [selectedWorkout, setSelectedWorkout] = useState<Workout | null>(null);
  const [durationMinutes, setDurationMinutes] = useState(60);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    const fetchWorkouts = async () => {
      setLoading(true);
      try {
        const data = await workoutService.searchWorkouts(query);
        setWorkouts(data);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };
    
    const timer = setTimeout(() => {
      fetchWorkouts();
    }, 300);
    
    return () => clearTimeout(timer);
  }, [query]);

  const handleSubmit = async () => {
    if (!selectedWorkout) return;
    setSubmitting(true);
    try {
      await onSelect(selectedWorkout.id, durationMinutes);
      onClose();
    } catch (error) {
      console.error(error);
      setSubmitting(false);
    }
  };

  const calculatedBurn = selectedWorkout ? Math.round(selectedWorkout.caloriesBurned * (durationMinutes / 60)) : 0;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm p-4">
      <div className="bg-white rounded-2xl shadow-xl w-full max-w-lg overflow-hidden flex flex-col max-h-[85vh]">
        <div className="p-4 border-b border-zinc-100 flex justify-between items-center bg-zinc-50/50">
          <h3 className="text-lg font-display font-semibold text-zinc-900">Log a Workout</h3>
          <button onClick={onClose} className="text-zinc-400 hover:text-zinc-600 transition-colors">
            ✕
          </button>
        </div>

        {!selectedWorkout ? (
          <div className="flex flex-col flex-grow overflow-hidden">
            <div className="p-4 border-b border-zinc-100">
              <div className="relative">
                <Search className="w-5 h-5 absolute left-3 top-1/2 -translate-y-1/2 text-zinc-400" />
                <input
                  type="text"
                  placeholder="Search workouts..."
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
              ) : workouts.length === 0 ? (
                <div className="text-center p-8 text-zinc-500">No workouts found.</div>
              ) : (
                <div className="space-y-1">
                  {workouts.map(workout => (
                    <button
                      key={workout.id}
                      onClick={() => setSelectedWorkout(workout)}
                      className="w-full flex items-center p-3 rounded-xl hover:bg-zinc-50 transition-colors text-left"
                    >
                      <div className="w-12 h-12 rounded-lg bg-blue-50 flex flex-shrink-0 items-center justify-center text-blue-600 mr-4">
                        <Activity className="w-6 h-6" />
                      </div>
                      <div>
                        <div className="font-semibold text-zinc-900">{workout.type}</div>
                        <div className="text-xs text-zinc-500">{workout.caloriesBurned} kcal / hour</div>
                      </div>
                    </button>
                  ))}
                </div>
              )}
            </div>
          </div>
        ) : (
          <div className="p-6 flex flex-col items-center">
            <div className="w-20 h-20 rounded-2xl bg-blue-50 flex items-center justify-center text-blue-600 mb-4 shadow-sm border border-blue-100">
              <Activity className="w-10 h-10" />
            </div>
            <h3 className="text-xl font-display font-bold text-zinc-900">{selectedWorkout.type}</h3>
            <p className="text-sm text-zinc-500 mb-6">{selectedWorkout.caloriesBurned} kcal per hour</p>
            
            <div className="w-full space-y-4 bg-zinc-50 p-4 rounded-xl border border-zinc-100">
              <label className="label text-center">Duration (Minutes)</label>
              <div className="flex items-center justify-center space-x-4">
                <button 
                  onClick={() => setDurationMinutes(Math.max(1, durationMinutes - 15))}
                  className="w-10 h-10 rounded-full bg-white border border-zinc-200 text-zinc-600 font-bold hover:bg-zinc-50 hover:border-zinc-300 transition-colors shadow-sm"
                >
                  -
                </button>
                <input 
                  type="number" 
                  min="1"
                  value={durationMinutes}
                  onChange={(e) => setDurationMinutes(Math.max(1, parseInt(e.target.value) || 1))}
                  className="w-24 text-center input-field font-bold text-lg"
                />
                <button 
                  onClick={() => setDurationMinutes(durationMinutes + 15)}
                  className="w-10 h-10 rounded-full bg-white border border-zinc-200 text-zinc-600 font-bold hover:bg-zinc-50 hover:border-zinc-300 transition-colors shadow-sm"
                >
                  +
                </button>
              </div>
              
              <div className="flex justify-between items-center pt-4 mt-2 border-t border-zinc-200">
                <div className="text-sm text-zinc-600">Total Burned:</div>
                <div className="text-right">
                  <span className="font-bold text-orange-600 text-lg">{calculatedBurn} kcal</span>
                </div>
              </div>
            </div>
            
            <div className="flex w-full space-x-3 mt-6">
              <button onClick={() => setSelectedWorkout(null)} className="btn-secondary flex-1">Back</button>
              <button onClick={handleSubmit} disabled={submitting} className="btn-primary flex-1 flex justify-center items-center">
                {submitting ? <Loader2 className="w-5 h-5 animate-spin" /> : 'Log Workout'}
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default WorkoutSelector;
