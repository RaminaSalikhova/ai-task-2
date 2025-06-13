# User Management App

A responsive React + TypeScript application for displaying and managing user data from the [JSONPlaceholder](https://jsonplaceholder.typicode.com/) API. The app features a professional, modern UI with a table layout, modal user details, and client-side user management.

## Features
- **User List Table:**
  - Displays users in a clean, responsive table layout
  - Columns: Name/Email, Address, Phone, Website, Company
  - Delete users (sends real DELETE request to JSONPlaceholder)
- **User Detail Modal:**
  - Click a user to view all details in a modal
  - Includes address, contact, company, and a map link
  - Accessible, animated, and mobile-friendly
- **Modern UI:**
  - Styled with CSS Modules
  - Responsive and mobile-first
  - Subtle animations and visual feedback

## Screenshots

### User List Table
![User List Table](./screenshots/user-list.png)

### User Detail Modal
![User Detail Modal](./screenshots/user-modal.png)

> **Note:** Place your screenshots in a `screenshots/` folder inside the project root. Example: `user-management/screenshots/user-list.png`

## Getting Started

1. **Install dependencies:**
   ```sh
   npm install
   ```
2. **Run the app:**
   ```sh
   npm start
   ```
   The app will open at [http://localhost:3000](http://localhost:3000).

## API Reference
- Uses [JSONPlaceholder](https://jsonplaceholder.typicode.com/guide/) `/users` endpoint
- Delete operations send a real DELETE request (faked by JSONPlaceholder)

## Project Structure
```
user-management/
├── src/
│   ├── components/
│   │   ├── user-table/
│   │   └── user-modal/
│   ├── types/
│   ├── utils/
│   ├── App.tsx
│   └── ...
├── public/
├── screenshots/
│   ├── user-list.png
│   └── user-modal.png
└── README.md
```

## License
MIT
