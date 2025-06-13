import React, { useEffect, useState } from 'react';
import { fetchUsers } from './utils/api';
import { User } from './types/user';
import UserTable from './components/user-table/UserTable';
import UserModal from './components/user-modal/UserModal';
import styles from './App.module.css';

const App: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [hasError, setHasError] = useState(false);
  const [selectedUser, setSelectedUser] = useState<User | null>(null);

  useEffect(() => {
    fetchUsers()
      .then(setUsers)
      .catch(() => setHasError(true))
      .finally(() => setIsLoading(false));
  }, []);

  const handleUserClick = (user: User) => setSelectedUser(user);
  const handleCloseModal = () => setSelectedUser(null);
  const handleDeleteUser = async (userId: number) => {
    try {
      const response = await fetch(`https://jsonplaceholder.typicode.com/users/${userId}`, {
        method: 'DELETE',
      });
      if (response.ok) {
        setUsers((prev) => prev.filter((u) => u.id !== userId));
        if (selectedUser?.id === userId) setSelectedUser(null);
      } else {
        alert('Failed to delete user on server.');
      }
    } catch (error) {
      alert('Network error while deleting user.');
    }
  };

  return (
    <div className={styles.appRoot}>
      <header className={styles.header}>
        <h1>User Management</h1>
        <p>Responsive app with professional UI, modal details, and user management.</p>
      </header>
      <main>
        {isLoading && <div className={styles.center}>Loading users...</div>}
        {hasError && <div className={styles.centerError}>Failed to load users.</div>}
        {!isLoading && !hasError && (
          <UserTable users={users} onUserClick={handleUserClick} onDeleteUser={handleDeleteUser} />
        )}
        <UserModal user={selectedUser} onClose={handleCloseModal} />
      </main>
      <footer className={styles.footer}>
        <span>Powered by JSONPlaceholder & React</span>
      </footer>
    </div>
  );
};

export default App;
