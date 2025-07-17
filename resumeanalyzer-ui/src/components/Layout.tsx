import React from 'react';

export default function Layout({ children }: { children: React.ReactNode }) {
    return (
        <div className="flex flex-col min-h-screen">
            <nav className="bg-blue-600 text-white px-4 py-2">
                <span className="font-bold text-lg">ResumeAnalyzer</span>
            </nav>
            <main className="flex-1 p-4">
                {children}
            </main>
            <footer className="bg-gray-200 text-center py-2 mt-auto">
                &copy; {new Date().getFullYear()} ResumeAnalyzer
            </footer>
        </div>
    );
}
