import { useState, useEffect } from "react";
import axios from "axios";
import "./App.css";

function App() {
  const [teams, setTeams] = useState([]);

  const [match, setMatch] = useState([]);
  const [matchdata, setMatchData] = useState([]);

  const [selectedTeam1, setSelectedTeam1] = useState("");
  const [selectedTeam2, setSelectedTeam2] = useState("");

  const [players, setPlayers] = useState({ team1: [], team2: [] });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const [selectedFields, setSelectedFields] = useState([]);
  const [availableFields, setAvailableFields] = useState([]);
  const [data, setData] = useState([]);
  const [isOpen, setIsOpen] = useState(false);

  const [sortField, setSortField] = useState("points_per_match");
  const handleSort = (field) => {
    setSortField(field);
  };

  const getFieldLabel = (field) => {
    const labels = {
      points_per_match: "PPM",
      weighted_ppm: "WeightedPPM",
      adjusted_ppm: "AdjustedPPM",
      experience_boosted_ppm: "ExperienceBoostedPPM",
      total_sum: "TotalPPM",
    };
    return labels[field] || field;
  };

  useEffect(() => {
    const fetchFields = async () => {
      try {
        const response = await axios.get("/api/players/fields");
        setAvailableFields(response.data);
      } catch (error) {
        console.error("Error fetching fields:", error);
      }
    };
    fetchFields();
  }, []);

  console.log(selectedFields);

  const fetchData = async () => {
    try {
      const response = await axios.get("api/players/dynamic", {
        params: {
          fields: selectedFields.join(","),
        },
      });
      setData(response.data);
    } catch (error) {
      console.error("Error fetching players:", error);
    }
  };

  const toggleField = (field) => {
    setSelectedFields((prev) => {
      if (prev.includes(field)) {
        return prev.filter((f) => f !== field);
      }
      return [...prev, field];
    });
  };

  useEffect(() => {
    const fetchTeams = async () => {
      try {
        const matches = await axios.get("/api/matches");
        setMatch(matches.data);
      } catch (err) {
        try {
          const response = await axios.get("/api/teams");
          setTeams(response.data);
        } catch (err) {
          setError("Failed to fetch teams");
          console.error(err);
        }
      }
    };
    fetchTeams();
  }, []);

  useEffect(() => {
    if (!match || match.length === 0) return;
    const date = new Date();
    const currTime = date.getHours() * 60 + date.getMinutes();
    if (currTime <= (15 + 3) * 60 + 30 && match.length >= 2) {
      setSelectedTeam1(match[0].team1);
      setSelectedTeam2(match[0].team2);
      setMatchData(match[0]);
      handleSubmit();
      return;
    } else if (currTime > 18 * 60 + 30 && currTime <= 23 * 60 + 59) {
      setSelectedTeam1(match.length == 2 ? match[1].team1 : match[0].team1);
      setSelectedTeam2(match.length == 2 ? match[1].team2 : match[0].team2);
      setMatchData(match.length == 2 ? match[1] : match[0]);
      handleSubmit();
      return;
    }
  }, [match]);

  const handleSubmit = async () => {
    setLoading(true);
    setError("");
    setPlayers({ team1: [], team2: [] });

    try {
      const response = await axios.get("/api/players", {
        params: { team1: selectedTeam1, team2: selectedTeam2 },
      });
      setPlayers(response.data);
    } catch (err) {
      setError("Failed to fetch players data");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <div className="container">
        <h1>Team Comparison Tool</h1>

        <div className="controls">
          <select
            value={selectedTeam1}
            onChange={(e) => setSelectedTeam1(e.target.value)}
            disabled={loading}
          >
            <option value="">Select Team 1</option>
            {teams.map((team) => (
              <option key={team} value={team}>
                {team}
              </option>
            ))}
          </select>

          <select
            value={selectedTeam2}
            onChange={(e) => setSelectedTeam2(e.target.value)}
            disabled={loading}
          >
            <option value="">Select Team 2</option>
            {teams.map((team) => (
              <option key={team} value={team}>
                {team}
              </option>
            ))}
          </select>

          <button onClick={handleSubmit} disabled={loading}>
            {loading ? "Loading..." : "Compare Teams"}
          </button>

          <div className="dropdown">
            <button
              onClick={() => setIsOpen(!isOpen)}
              className="dropdown-toggle"
            >
              Select Fields ({selectedFields.length})
            </button>
          </div>

          <button onClick={() => handleSort("points_per_match")}>
            Sort By PPM
          </button>
          <button onClick={() => handleSort("weighted_ppm")}>
            Sort By WeightedPPM
          </button>
          <button onClick={() => handleSort("adjusted_ppm")}>
            Sort By AdjustedPPM
          </button>
          <button onClick={() => handleSort("experience_boosted_ppm")}>
            Sort By ExperiencedPPM
          </button>
          <button onClick={() => handleSort("total_sum")}>
            Sort By TotalPPM
          </button>
        </div>

        {isOpen && (
          <div className="dropdown-menu">
            {availableFields.map((field) => (
              <label key={field} className="dropdown-item">
                <input
                  type="checkbox"
                  checked={selectedFields.includes(field)}
                  onChange={() => toggleField(field)}
                />
                {field}
              </label>
            ))}
            <button
              onClick={() => {
                fetchData();
                setIsOpen(false);
              }}
              className="apply-button"
            >
              Apply
            </button>
          </div>
        )}

        {console.log(data)}

        <div className="container-matchdata">
          <div className="matchdata">
            <div>
              {matchdata?.matchdate && matchdata?.matchtime
                ? `${matchdata.matchdate},   ${matchdata.matchtime}`
                : ""}
            </div>
            <div>
              {matchdata?.stadium && matchdata?.city
                ? `${matchdata.stadium},  ${matchdata.city}`
                : ""}
            </div>
          </div>
        </div>

        {error && <div className="error">{error}</div>}

        <div className="results">
          <div className="team-list">
            <h2>{selectedTeam1} Players</h2>
            {players.team1.map((player) => (
              <div key={player.id} className="player-card">
                <span>{player.playerName}</span>
                <span>{player.playerType}</span>
                <span>PPM: {player.points_per_match}</span>
              </div>
            ))}
          </div>

          <div className="team-list">
            <h2>{selectedTeam2} Players</h2>
            {players.team2.map((player) => (
              <div key={player.id} className="player-card">
                <span>{player.playerName}</span>
                <span>{player.playerType}</span>
                <span>PPM: {player.points_per_match}</span>
              </div>
            ))}
          </div>

          <div className="top-players team-list2">
            <h2>Top Players</h2>

            {Object.values(
              [...players.team1, ...players.team2].reduce((acc, player) => {
                if (acc[player.playerName]) {
                  // Sum all fields you want to sort by
                  {
                    console.log(player);
                  }

                  acc[player.playerName].points_per_match +=
                    player.points_per_match;
                  acc[player.playerName].weighted_ppm += player.weighted_ppm;
                  acc[player.playerName].adjusted_ppm += player.adjusted_ppm;
                  acc[player.playerName].experience_boosted_ppm +=
                    player.experience_boosted_ppm;
                } else {
                  acc[player.playerName] = { ...player };
                }
                return acc;
              }, {})
            )

              .sort((a, b) => b[sortField] - a[sortField]) // Dynamic sort
              .map((player) => (
                <div key={player.playerName} className="player-card2">
                  <span>{player.playerName}</span>
                  <span>
                    {getFieldLabel(sortField)}: {player[sortField]}
                  </span>
                </div>
              ))}
          </div>
        </div>
      </div>

      <table className="data-table">
        <thead>
          <tr>
            {data[0] &&
              Object.keys(data[0]).map((key) => <th key={key}>{key}</th>)}
          </tr>
        </thead>
        <tbody>
          {data.map((player, index) => (
            <tr key={index}>
              {Object.values(player).map((value, i) => (
                <td key={i}>{value?.toString()}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </>
  );
}

export default App;
