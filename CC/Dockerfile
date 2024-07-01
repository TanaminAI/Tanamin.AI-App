# Gunakan gambar resmi Node.js.
# https://hub.docker.com/_/node
FROM node:18-slim

# Buat dan pindah ke direktori aplikasi.
WORKDIR /usr/src/app

# Salin manifest dependensi aplikasi ke gambar kontainer.
# Menggunakan wildcard untuk memastikan baik package.json maupun package-lock.json disalin.
COPY package*.json ./

# Install dependensi produksi.
RUN npm install --production

# Salin kode lokal ke gambar kontainer.
COPY . .

# Jalankan layanan web saat kontainer di mulai.
CMD [ "node", "server.js" ]

# Informasikan Docker bahwa kontainer mendengarkan pada port 3000.
EXPOSE 3000
