import React, { useState, useEffect } from "react";
import "./App.css";

// Define the Task type
interface Task {
  id: string;
  description: string;
  completed: boolean;
  createdAt: string;
  updatedAt: string;
}

const API_BASE_URL = "http://localhost:3080";

function App() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [newTaskDesc, setNewTaskDesc] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  // Fetch tasks from the backend
  const fetchTasks = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`${API_BASE_URL}/tasks`);
      if (!response.ok) {
        throw new Error(`Error ${response.status}: ${response.statusText}`);
      }
      const data: Task[] = await response.json();
      setTasks(data);
    } catch (err: any) {
      setError(`Failed to fetch tasks: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  // Create a new task
  const handleCreateTask = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!newTaskDesc.trim()) return;
    try {
      const response = await fetch(`${API_BASE_URL}/tasks`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ description: newTaskDesc, completed: false }),
      });
      if (!response.ok) {
        throw new Error(`Error ${response.status}: ${response.statusText}`);
      }
      setNewTaskDesc("");
      fetchTasks();
    } catch (err: any) {
      setError(`Failed to create task: ${err.message}`);
    }
  };

  return (
    <div className="App" style={{ padding: "20px" }}>
      <h1>Task Manager</h1>
      {error && <p style={{ color: "red" }}>Error: {error}</p>}
      <form onSubmit={handleCreateTask} style={{ marginBottom: "20px" }}>
        <input
          type="text"
          placeholder="New task description"
          value={newTaskDesc}
          onChange={(e) => setNewTaskDesc(e.target.value)}
          style={{ marginRight: "10px", padding: "5px" }}
        />
        <button type="submit" style={{ padding: "5px 10px" }}>
          Add Task
        </button>
      </form>
      {loading ? (
        <p>Loading tasks...</p>
      ) : tasks.length === 0 ? (
        <p>No tasks found. Add your first task!</p>
      ) : (
        <ul>
          {tasks.map((task) => (
            <li key={task.id}>
              {task.description} - {task.completed ? "Completed" : "Not Completed"}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default App;
