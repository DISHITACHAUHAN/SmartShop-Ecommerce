# E-Commerce Platform - Frontend

React frontend for the E-Commerce Platform.

## Technology Stack

- React 18
- Redux Toolkit
- React Router
- Axios
- Tailwind CSS
- Vite

## Project Structure

```
src/
├── components/      # Reusable components
│   ├── common/      # Common UI components
│   ├── auth/        # Authentication components
│   ├── products/    # Product components
│   ├── cart/        # Cart components
│   ├── checkout/    # Checkout components
│   ├── payment/     # Payment components
│   └── orders/      # Order components
├── pages/           # Page components
├── store/           # Redux store and slices
├── services/        # API service layer
└── utils/           # Utility functions
```

## Setup

1. Install dependencies:
```bash
npm install
```

2. Start development server:
```bash
npm run dev
```

3. Build for production:
```bash
npm run build
```

## Environment Variables

Create `.env` file:
```
VITE_API_URL=http://localhost:8080/api
```

## Testing

Run tests:
```bash
npm test
```

## Linting

Run ESLint:
```bash
npm run lint
```
