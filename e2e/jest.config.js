module.exports = {
  preset: 'ts-jest',
  testEnvironment: 'node',
  verbose: true,
  maxConcurrency: 1,
  setupFiles: [
      "dotenv/config"
  ]
};
