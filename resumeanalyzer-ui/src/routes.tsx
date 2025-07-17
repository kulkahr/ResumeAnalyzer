import { BrowserRouter, Routes, Route } from 'react-router-dom';
import App from './App';
import Layout from './components/Layout';

export default function AppRoutes() {
    return (
        <BrowserRouter>
            <Layout>
                <Routes>
                    <Route path="/" element={<App />} />
                    {/* Add more routes here as needed */}
                </Routes>
            </Layout>
        </BrowserRouter>
    );
}
