import { useEffect, useState } from "react";
import { FaCircle } from "react-icons/fa";
import Dashboard from "./Dashboard";

function App() {
  const [message, setMessage] = useState("");
  const [isHealthy, setIsHealthy] = useState(false);

  useEffect(() => {
    fetch("http://localhost:8080/api/health")
      .then((res) => {
        if (!res.ok) throw new Error();
        return res.text();
      })
      .then((data) => {
        setMessage(data);
        setIsHealthy(true);
      })
      .catch(() => {
        setMessage("Backend is down");
        setIsHealthy(false);
      });
  }, []);

  return (
    <>
      <h5 style={{ gap: "6px" }}>
        {isHealthy && (
          <FaCircle size={8} color="green" />
        )}
        {message}
      </h5>

      <Dashboard />
    </>
  );
}

export default App;