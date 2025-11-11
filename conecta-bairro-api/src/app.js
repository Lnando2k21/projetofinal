const express = require('express');
const cors = require('cors');
const dotenv = require('dotenv');

dotenv.config();

const app = express();

app.use(cors());
app.use(express.json());

// Rotas
const indexRoutes = require('./routes/index.routes');
app.use('/api', indexRoutes);

// Middleware de erro simples
const { errorHandler } = require('./middleware/errorHandler');
app.use(errorHandler);

module.exports = app;
