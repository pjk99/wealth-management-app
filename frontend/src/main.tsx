import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { ModuleRegistry, AllCommunityModule } from "ag-grid-community";

import { RowGroupingModule } from 'ag-grid-enterprise'; 
import { PivotModule } from 'ag-grid-enterprise'; 
import { TreeDataModule } from 'ag-grid-enterprise'; 

ModuleRegistry.registerModules([ AllCommunityModule, RowGroupingModule, PivotModule, TreeDataModule ]); 

// Register all community features

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
