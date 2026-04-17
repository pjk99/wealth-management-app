import React, { useMemo } from "react";
import { AgGridReact } from "ag-grid-react";
import type { ColDef } from "ag-grid-community";

import {
  ModuleRegistry,
  AllCommunityModule,
} from "ag-grid-community";

ModuleRegistry.registerModules([AllCommunityModule]);

import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";

// TYPES

type Account = {
  id: number;
  accountNumber: string;
  accountType: string;
  custodian: string;
  accountValue: number | null;
  ownershipPercentage: number | null;
};

type Member = {
  id: number;
  name: string;
  relationship: string | null;
  accounts: Account[];
};

type Household = {
  id: number;
  name: string;
  income: number;
  netWorth: number;
  members: Member[];
};

type Props = {
  data: Household[];
};


const HouseholdGrid: React.FC<Props> = ({ data }) => {
 
  const rowData = useMemo(() => {
    const rows: any[] = [];

    data.forEach((household) => {
      household.members.forEach((member) => {
        member.accounts.forEach((account) => {
          rows.push({
            // ===== LEVEL 0 (HOUSEHOLD) =====
            householdId: household.id,
            householdName: household.name,
            householdIncome: household.income,
            householdNetWorth: household.netWorth,

            // ===== LEVEL 1 (MEMBER) =====
            memberId: member.id,
            memberName: member.name,
            relationship: member.relationship,

            // ===== LEVEL 2 (ACCOUNT) =====
            accountId: account.id,
            accountNumber: account.accountNumber,
            accountType: account.accountType,
            custodian: account.custodian,
            accountValue: account.accountValue,
            ownershipPercentage: account.ownershipPercentage,
          });
        });
      });
    });

    return rows;
  }, [data]);


  const columnDefs: ColDef[] = [
  //  HOUSEHOLD LEVEL
  {
    headerName: "Household",
    field: "householdName",
    rowGroup: true,
    hide: true,
    valueGetter: (p) => p.data?.householdName,
  },

  //  MEMBER LEVEL
  {
    headerName: "Member",
    field: "memberName",
    rowGroup: true,
    hide: true,
    valueGetter: (p) => p.data?.memberName,
  },

  //  ACCOUNT LEVEL
  {
    headerName: "Account #",
    field: "accountNumber",
  },
  {
    headerName: "Type",
    field: "accountType",
  },
  {
    headerName: "Custodian",
    field: "custodian",
  },
  {
    headerName: "Value",
    field: "accountValue",
  },
];

  const defaultColDef: ColDef = {
    sortable: true,
    filter: true,
    resizable: true,
  };

  return (
    <div className="ag-theme-alpine" style={{ height: 700, width: "100%" }}>
      <AgGridReact
  rowData={rowData}
  columnDefs={columnDefs}
  defaultColDef={defaultColDef}

  groupDisplayType="groupRows"
  groupDefaultExpanded={1}

  autoGroupColumnDef={{
    headerName: "Hierarchy",
    minWidth: 350,
    cellRendererParams: {
      suppressCount: false,
    },
  }}
/>
    </div>
  );
};

export default HouseholdGrid;