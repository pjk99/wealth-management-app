import { useState, useEffect } from "react";
import axios from "axios";

import {
  Container,
  Typography,
  Button,
  Box,
  CircularProgress
} from "@mui/material";
import HouseholdGrid from "./HouseholdGrid";

export default function Dashboard() {
  const [file, setFile] = useState<File | null>(null);
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchData = async () => {
    const res = await axios.get("http://localhost:8080/api/households");
    setData(res.data);
  };

  useEffect(() => {
    fetchData();
  }, []);

  

  const handleUpload = async () => {
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);

    setLoading(true);

    try {
      await axios.post("http://localhost:8080/api/excel/upload", formData);
      await fetchData();
    } catch (err) {
      console.error(err);
      alert("Upload failed");
    }

    setLoading(false);
  };

  return (
    <Container maxWidth="md" sx={{ mt: 4 }}>
      <Typography variant="h4" gutterBottom>
        Wealth Manager Dashboard
      </Typography>

      {/* Upload Section */}
      <Box sx={{ mb: 3 }}>
        <input
          type="file"
          accept=".xlsx"
          onChange={(e) => {
  const files = e.target.files;
  if (!files || files.length === 0) return;

  setFile(files[0]);
}}
        />

        <Button
          variant="contained"
          sx={{ ml: 2 }}
          onClick={handleUpload}
          disabled={loading}
        >
          {loading ? "Uploading..." : "Upload Excel"}
        </Button>
      </Box>

      {loading && <CircularProgress />}
      <HouseholdGrid data={data} />
    </Container>
  );
}