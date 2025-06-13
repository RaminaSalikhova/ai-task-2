import React from 'react';
import { User } from '../../types/user';
import styles from './UserTable.module.css';

interface UserTableProps {
  users: User[];
  onUserClick: (user: User) => void;
  onDeleteUser: (userId: number) => void;
}

const UserTable: React.FC<UserTableProps> = ({ users, onUserClick, onDeleteUser }) => {
  return (
    <div className={styles.tableWrapper}>
      <div className={styles.table} role="table" aria-label="User List">
        <div className={styles.header} role="row">
          <div className={styles.cell}>Name / Email</div>
          <div className={styles.cell}>Address</div>
          <div className={styles.cell}>Phone</div>
          <div className={styles.cell}>Website</div>
          <div className={styles.cell}>Company</div>
          <div className={styles.cell}></div>
        </div>
        {users.map((user) => (
          <div className={styles.row} role="row" key={user.id}>
            <div className={styles.cell} onClick={() => onUserClick(user)} tabIndex={0} role="button">
              <div className={styles.name}>{user.name}</div>
              <div className={styles.email}>{user.email}</div>
            </div>
            <div className={styles.cell} onClick={() => onUserClick(user)} tabIndex={0} role="button">
              {user.address.street}, {user.address.suite}, {user.address.city}
            </div>
            <div className={styles.cell} onClick={() => onUserClick(user)} tabIndex={0} role="button">
              {user.phone}
            </div>
            <div className={styles.cell} onClick={() => onUserClick(user)} tabIndex={0} role="button">
              {user.website}
            </div>
            <div className={styles.cell} onClick={() => onUserClick(user)} tabIndex={0} role="button">
              {user.company.name}
            </div>
            <div className={styles.cell}>
              <button className={styles.deleteBtn} onClick={() => onDeleteUser(user.id)} aria-label={`Delete ${user.name}`}>Delete</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default UserTable; 