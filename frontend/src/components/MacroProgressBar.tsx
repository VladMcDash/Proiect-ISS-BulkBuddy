import React from 'react';
import { clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';

interface MacroProgressBarProps {
  label: string;
  consumed: number;
  goal: number;
  unit: string;
  colorClass: string;
  subLabel?: string;
}

const MacroProgressBar: React.FC<MacroProgressBarProps> = ({ 
  label, 
  consumed, 
  goal, 
  unit, 
  colorClass,
  subLabel 
}) => {
  // Prevent division by zero and cap at 100%
  const percentage = goal > 0 ? Math.min((consumed / goal) * 100, 100) : 0;
  const isOver = consumed > goal;
  
  return (
    <div className="flex flex-col space-y-2">
      <div className="flex justify-between items-end">
        <div>
          <span className="text-sm font-semibold text-zinc-700">{label}</span>
          {subLabel && <span className="text-xs text-zinc-500 ml-2">{subLabel}</span>}
        </div>
        <div className="text-right">
          <span className={twMerge("text-lg font-bold", isOver ? "text-error" : "text-zinc-900")}>
            {consumed}
          </span>
          <span className="text-sm text-zinc-500 ml-1">/ {goal} {unit}</span>
        </div>
      </div>
      
      <div className="h-3 w-full bg-zinc-100 rounded-full overflow-hidden relative">
        <div 
          className={twMerge("h-full rounded-full transition-all duration-1000 ease-out", colorClass, isOver ? "bg-error" : "")}
          style={{ width: `${percentage}%` }}
        />
        {isOver && (
          <div 
            className="absolute top-0 right-0 h-full bg-red-400 opacity-50"
            style={{ width: `${Math.min(((consumed - goal) / goal) * 100, 100)}%` }}
          />
        )}
      </div>
      
      {isOver && (
        <p className="text-xs font-medium text-error flex justify-end">
          Over goal by {consumed - goal} {unit}
        </p>
      )}
    </div>
  );
};

export default MacroProgressBar;
