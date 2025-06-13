import React, { useEffect } from 'react';
import { User } from '../../types/user';
import styles from './UserModal.module.css';

interface UserModalProps {
  user: User | null;
  onClose: () => void;
}

const UserModal: React.FC<UserModalProps> = ({ user, onClose }) => {
  useEffect(() => {
    const handleEsc = (e: KeyboardEvent) => {
      if (e.key === 'Escape') onClose();
    };
    document.addEventListener('keydown', handleEsc);
    return () => document.removeEventListener('keydown', handleEsc);
  }, [onClose]);

  if (!user) return null;

  const mapUrl = `https://www.google.com/maps?q=${user.address.geo.lat},${user.address.geo.lng}`;

  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.modal} onClick={e => e.stopPropagation()} role="dialog" aria-modal="true" aria-labelledby="user-modal-title">
        <button className={styles.closeBtn} onClick={onClose} aria-label="Close">&times;</button>
        <h2 id="user-modal-title" className={styles.title}>{user.name}</h2>
        <a href={`mailto:${user.email}`} className={styles.email}>{user.email}</a>
        <section className={styles.section}>
          <h3>Address</h3>
          <div>{user.address.street}, {user.address.suite}</div>
          <div>{user.address.city}, {user.address.zipcode}</div>
          <a href={mapUrl} target="_blank" rel="noopener noreferrer" className={styles.mapLink}>📍 View on map</a>
        </section>
        <section className={styles.section}>
          <h3>Contact</h3>
          <div><b>Phone:</b> {user.phone}</div>
          <div><b>Website:</b> <a href={`http://${user.website}`} target="_blank" rel="noopener noreferrer">{user.website}</a></div>
        </section>
        <section className={styles.section}>
          <h3>Company</h3>
          <div><b>Name:</b> {user.company.name}</div>
          <div><b>Catchphrase:</b> {user.company.catchPhrase}</div>
          <div><b>Business:</b> {user.company.bs}</div>
        </section>
      </div>
    </div>
  );
};

export default UserModal; 