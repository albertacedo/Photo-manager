const { Client } = require('pg')
const password = process.env.POSTGRES_PASSWORD

let client
 const init = async () => {
    client = new Client({user: 'postgres', host: 'postgresql-postgresql-svc', database: 'transcode', password:password})
    try {
        await client.connect()
    } 
    catch (err) {
        console.log(err.stack)
    }
}

const executeQuery = async (query,values) => {
    try {
        const res = await client.query(query,values)
        return res.rows
    } catch (err) {
        console.log(err.stack)
    }
}

module.exports = {
    init,
    executeQuery
}
    
    

